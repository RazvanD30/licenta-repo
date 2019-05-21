export const APP_SETTINGS = {

  URLS: {
    AUTHENTICATION: {
      LOGIN: 'http://localhost:8080/authentication/login',
      REGISTER: 'http://localhost:8080/authentication/register',
      LOGOUT: 'http://localhost:8080/authentication/logout'
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
      GET_ALL: 'http://localhost:8080/network-management/networks',
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




