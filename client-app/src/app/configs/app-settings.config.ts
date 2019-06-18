
const BASE_URL = 'http://localhost:8803';
const AUTHORIZATION_SERVICE_URL = BASE_URL + '/authentication';
const NETWORK_SERVICE_URL = BASE_URL + '/network-configuration';
const BRANCH_MANAGEMENT_URL = BASE_URL + '/branch-management';

export const APP_SETTINGS = {
  URLS: {
    AUTHENTICATION: {
      TOKEN_REQUEST: AUTHORIZATION_SERVICE_URL + '/oauth/token', // AUTHORIZATION_SERVICE_URL + '/oauth/token/',
      LOGIN: 'http://localhost:8803/authentication/requestTokens',
      REGISTER: 'http://localhost:8080/authentication/register',
      LOGOUT: 'http://localhost:8080/authentication/logout',
      GET_TOKEN: 'http://localhost:8804/spring-security-oauth-server/oauth/token'
    },
    BRANCH_MANAGEMENT: {
      GET_ALL_FOR_OWNER: BRANCH_MANAGEMENT_URL + '/withOwner/',
      GET_ALL_FOR_CONTRIBUTOR: BRANCH_MANAGEMENT_URL + '/withContributor/',
      GET_ALL_FOR_USER: BRANCH_MANAGEMENT_URL + '/withUser/',
      GET_BY_ID: BRANCH_MANAGEMENT_URL + '/',
      GET_BY_NAME: BRANCH_MANAGEMENT_URL + '/withName/',
      POST_CREATE: BRANCH_MANAGEMENT_URL,
      PUT_UPDATE: BRANCH_MANAGEMENT_URL,
      DELETE_DELETE: BRANCH_MANAGEMENT_URL + '/'
    },
    USER_MANAGEMENT: {
      GET_ALL: 'http://localhost:8080/users',
      GET_BY_ID: 'http://localhost:8080/users/',
      UPDATE: 'http://localhost:8080/users/',
      DELETE: 'http://localhost:8080/users/'
    },
    NETWORK_INIT: {
      CREATE: 'http://localhost:8080/network-management/init',
      GET_ALL: 'http://localhost:8080/network-management/init',
      GET_BY_ID: 'http://localhost:8080/network-management/init/',
      DELETE_BY_ID: 'http://localhost:8080/network-management/init/'
    },
    NETWORK_RUN: {
      GET_ALL: 'http://localhost:8803/network-configuration/networks',
      GET_BY_ID: 'http://localhost:8080/network-management/networks/',
      DELETE_BY_ID: 'http://localhost:8080/network-management/networks/',
      UPDATE: 'http://localhost:8080/network-management/networks',
      RUN_BY_ID: 'http://localhost:8080/network-management/networks/run/',
      SAVE_PROGRESS_BY_ID: 'http://localhost:8080/network-management/networks/save-progress/'
    },
    NETWORK_DEBUG: {
      GET_ALL: 'http://localhost:8080/network-management/networks',
      GET_CONNECTIONS: 'http://localhost:8080/network/connections',
      GET_BY_ID: '',
      UPDATE: '',
      DELETE: ''
    },
    NETWORK_LOGS: {
      GET_ALL_SORTED_BY_NET_ID: 'http://localhost:8080/network-management/logs/'
    }
  }
};




