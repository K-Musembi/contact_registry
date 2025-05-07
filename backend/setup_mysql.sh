#!/bin/bash

# --- Configuration ---
DB_NAME="my_db"
DB_USER="db_admin"      # This should match your application.properties: spring.datasource.username
DB_PASS="my_password"   # This should match your application.properties: spring.datasource.password
MYSQL_ROOT_USER="root"
# You might need to enter the MySQL root password when prompted by the script

# --- Helper Functions ---
execute_mysql_command() {
    local command_to_execute="$1"
    # Try with sudo first, then prompt for root password if that fails or is not preferred
    if sudo mysql -u "$MYSQL_ROOT_USER" -e "$command_to_execute"; then
        echo "SUCCESS: Executed command: $command_to_execute"
    elif mysql -u "$MYSQL_ROOT_USER" -p -e "$command_to_execute"; then
        echo "SUCCESS: Executed command (with password prompt): $command_to_execute"
    else
        echo "ERROR: Failed to execute command: $command_to_execute"
        echo "Please ensure MySQL root user credentials are correct and MySQL server is running."
        exit 1
    fi
}

# --- Main Script ---

echo "Starting MySQL setup script..."

# 1. Check for and install MySQL Server 8.0 if not present
if ! dpkg -s mysql-server-8.0 &> /dev/null; then
    echo "MySQL Server 8.0 not found. Attempting to install..."
    sudo apt-get update
    sudo apt-get install -y mysql-server-8.0
    if [ $? -ne 0 ]; then
        echo "ERROR: Failed to install MySQL Server 8.0. Please install it manually and re-run."
        exit 1
    fi
    echo "MySQL Server 8.0 installed successfully."
    echo "Starting MySQL service..."
    sudo systemctl start mysql
    sudo systemctl enable mysql # Ensure it starts on boot
    echo "Waiting for MySQL service to initialize after installation..."
    sleep 15 # Increased sleep time
else
    echo "MySQL Server 8.0 is already installed."
    # Ensure MySQL service is running even if already installed
    if ! sudo systemctl is-active --quiet mysql; then
        echo "MySQL service is not running. Attempting to start it..."
        sudo systemctl start mysql
        sleep 5
        if ! sudo systemctl is-active --quiet mysql; then
            echo "ERROR: Failed to start MySQL service."
            exit 1
        fi
    fi
fi


echo "Attempting to connect to MySQL as root to perform setup..."
echo "You might be prompted for the MySQL root password if not using sudo or if sudo doesn't grant passwordless access."

# 2. Create the database if it doesn't exist
echo "Creating database '$DB_NAME' if it doesn't exist..."
execute_mysql_command "CREATE DATABASE IF NOT EXISTS \`$DB_NAME\` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 3. Create the user if they don't exist and set password
echo "Creating user '$DB_USER'@'localhost' if they don't exist..."
# Check if user exists first to avoid error if re-running
# The subshell for user_exists_command ensures that `set -e` doesn't exit if the command fails due to auth
user_exists_command="SELECT EXISTS(SELECT 1 FROM mysql.user WHERE user = '$DB_USER' AND host = 'localhost');"
user_exists_output=$(sudo mysql -N -s -u "$MYSQL_ROOT_USER" -e "$user_exists_command" 2>/dev/null || mysql -N -s -u "$MYSQL_ROOT_USER" -p -e "$user_exists_command" 2>/dev/null)

if [ "$user_exists_output" = "0" ]; then
    execute_mysql_command "CREATE USER '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASS';"
    echo "User '$DB_USER'@'localhost' created."
elif [ "$user_exists_output" = "1" ]; then
    echo "User '$DB_USER'@'localhost' already exists. Attempting to update password (if different)."
    execute_mysql_command "ALTER USER '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASS';"
else
    echo "WARNING: Could not determine if user '$DB_USER'@'localhost' exists. Proceeding with caution."
    # Fallback: try to create or alter, one might succeed or provide a more specific error
    if ! execute_mysql_command "CREATE USER '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASS';"; then
        execute_mysql_command "ALTER USER '$DB_USER'@'localhost' IDENTIFIED BY '$DB_PASS';"
    fi
fi

# 4. Grant privileges to the user on the database
echo "Granting privileges to '$DB_USER' on database '$DB_NAME'..."
execute_mysql_command "GRANT ALL PRIVILEGES ON \`$DB_NAME\`.* TO '$DB_USER'@'localhost';"

# 5. Flush privileges to apply changes
echo "Flushing privileges..."
execute_mysql_command "FLUSH PRIVILEGES;"

echo "-----------------------------------------------------"
echo "MySQL setup script completed."
echo "Database: $DB_NAME"
echo "User:     $DB_USER (for localhost access)"
echo "Ensure your application.properties uses these credentials for the datasource."
echo "-----------------------------------------------------"

exit 0
