import axios from 'axios';
import API_BASE_URL from '../config';
// import { getToken } from '../utils/auth';

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
  const response = await axios.post(`${API_BASE_URL}/persons`, contactData);
  return response.data;
};

export const addCounty = async (countyData) => {
  const response = await axios.post(`${API_BASE_URL}/counties`, countyData);
  return response.data;
};

export const getContactsByCounty = async (countyName) => {
  const response = await axios.get(`${API_BASE_URL}/persons/${countyName}`);
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
