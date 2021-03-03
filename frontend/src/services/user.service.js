import axios from 'axios';
import authHeader from './auth-header';

const API_URL = 'http://localhost:8080/api/access/';

class UserService {

    getSearch() {
        return axios.get(API_URL + 'search', { headers: authHeader() });
    }

    getUpload() {
        return axios.get(API_URL + 'upload', { headers: authHeader() });
    }

    getManagement() {
        return axios.get(API_URL + 'management', { headers: authHeader() });
    }
}

export default new UserService();
