import axios from 'axios';
const API_BASE_URL = 'http://localhost:8080/api/v1';
// import { getToken } from '../utils/auth';

export const signUpUser = async (userDetails) => {
  const userDataJson = {
    username: userDetails.username,
    password: userDetails.password
  }
  const response = await axios.post(`${API_BASE_URL}/auth/signup`, userDataJson);
  return response.data;
}

export const loginUser = async (userDetails) => {
  const userDataJson = {
    username: userDetails.username,
    password: userDetails.password
  }
  const response = await axios.post(`${API_BASE_URL}/auth/login`, userDataJson);
  return response.data;
}

export const updateUser = async (userDetails) => {
  const response = await axios.get(`${API_BASE_URL}/users/${userDetails.username}`);
  const newResponse = await axios.put(`${API_BASE_URL}/users/${response.data.id}`, userDetails);
  return newResponse.data;
}

export const getGenderStats = async () => {
  const response = await axios.get(`${API_BASE_URL}/persons/gender-stats`);
  return response.data;
};

export const getTopCounties = async () => {
  const response = await axios.get(`${API_BASE_URL}/counties/top-counties`);
  return response.data;
};

export const getRecentContacts = async () => {
  const response = await axios.get(`${API_BASE_URL}/persons/five-recent`);
  return response.data;
};

export const addContact = async (contactData) => {
  const contactDataJson = {
    fullName: contactData.name,
    email: contactData.email,
    phone: contactData.phone,
    dateOfBirth: contactData.dateOfBirth,
    gender: contactData.gender,
    countyName: contactData.county,
  };

  const response = await axios.post(`${API_BASE_URL}/persons`, contactDataJson, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.data;
};

export const addCounty = async (countyName, countyCode) => {
  const countyData = {name: countyName, code: countyCode};
  const response = await axios.post(`${API_BASE_URL}/counties`, countyData, {
    headers: {
      'Content-Type': 'application/json',
    },
  });
  return response.data;
};

export const getContactsByCounty = async (countyName) => {
  const response = await axios.get(`${API_BASE_URL}/persons/county/${countyName}`);
  return response.data;
};

export const getAllCounties = async () => {
  const response = await axios.get(`${API_BASE_URL}/counties`);
  return response.data;
};

export const getContactDetails = async (contactId) => {
  const response = await axios.get(`${API_BASE_URL}/persons/${contactId}`);
  return response.data;
};

export const updateContact = async (contactId, contactData) => {
  const response = await axios.put(`${API_BASE_URL}/persons/${contactId}`, contactData);
  return response.data;
};
