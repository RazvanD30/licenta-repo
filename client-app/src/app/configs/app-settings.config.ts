
const BASE_URL = 'http://localhost:8803';
const AUTHORIZATION_SERVICE_URL = BASE_URL + '/authentication';
const BRANCH_MANAGEMENT_URL = BASE_URL + '/branch-management';
const FILE_MANAGEMENT_URL = BASE_URL + '/file-management';
const NETWORK_MANAGEMENT_URL = BASE_URL + '/network-management';
const NETWORK_INIT_URL = NETWORK_MANAGEMENT_URL + '/init';
const NETWORK_CONFIGURE_URL = NETWORK_MANAGEMENT_URL + '/configure';
const NETWORK_TRAIN_TEST_URL = NETWORK_MANAGEMENT_URL + '/train-test';
const NETWORK_LOG_URL = NETWORK_MANAGEMENT_URL + '/log';

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
      GET_WORKING_BRANCH: BRANCH_MANAGEMENT_URL + '/workingBranch/',
      POST_CREATE: BRANCH_MANAGEMENT_URL,
      POST_ASSIGN_WORKING_BRANCH: BRANCH_MANAGEMENT_URL + '/workingBranch/',
      PUT_UPDATE: BRANCH_MANAGEMENT_URL,
      DELETE_DELETE: BRANCH_MANAGEMENT_URL + '/'
    },
    FILE_MANAGEMENT: {
      GET_ALL: FILE_MANAGEMENT_URL,
      GET_BY_NAME: FILE_MANAGEMENT_URL + '/',
      GET_ALL_FOR_NETWORK_ID: FILE_MANAGEMENT_URL + '/run-configs/',
      GET_DOWNLOAD_FILE_BY_NAME: FILE_MANAGEMENT_URL + '/download/',
      POST_ADD_FILE: FILE_MANAGEMENT_URL,
      DELETE_FILE_BY_NAME: FILE_MANAGEMENT_URL + '/',
    },
    USER_MANAGEMENT: {
      GET_ALL: 'http://localhost:8080/users',
      GET_BY_ID: 'http://localhost:8080/users/',
      UPDATE: 'http://localhost:8080/users/',
      DELETE: 'http://localhost:8080/users/'
    },
    NETWORK_MANAGEMENT: {
      NETWORK_INIT: {
        GET_ALL: NETWORK_INIT_URL,
        GET_BY_ID: NETWORK_INIT_URL + '/',
        POST_CREATE: NETWORK_INIT_URL,
        DELETE_BY_ID: NETWORK_INIT_URL + '/'
      },
      NETWORK_CONFIGURE: {
        GET_ALL: NETWORK_CONFIGURE_URL,
        GET_ALL_FOR_USER: NETWORK_CONFIGURE_URL + '/withUser/',
        GET_BY_ID: NETWORK_CONFIGURE_URL + '/',
        GET_BY_NAME: NETWORK_CONFIGURE_URL + '/withName/',
        DELETE_BY_ID: NETWORK_CONFIGURE_URL + '/',
        PUT_UPDATE: NETWORK_CONFIGURE_URL,
        POST_LINK_FILE: NETWORK_CONFIGURE_URL + '/link',
        POST_UNLINK_FILE: NETWORK_CONFIGURE_URL + '/unlink'
      },
      NETWORK_TRAIN_TEST: {
        POST_RUN: NETWORK_TRAIN_TEST_URL + '/run/',
        POST_SAVE_PROGRESS: NETWORK_TRAIN_TEST_URL + '/save-progress/'
      },
      NETWORK_LOG: {
        GET_ALL_FOR_NETWORK_ID: NETWORK_LOG_URL + '/'
      }
    },

    NETWORK_RUN: {
      GET_ALL: 'http://localhost:8803/network-configuration/networks',
      GET_BY_ID: 'http://localhost:8080/network-initialization/networks/',
      DELETE_BY_ID: 'http://localhost:8080/network-initialization/networks/',
      UPDATE: 'http://localhost:8080/network-initialization/networks',
      RUN_BY_ID: 'http://localhost:8080/network-initialization/networks/run/',
      SAVE_PROGRESS_BY_ID: 'http://localhost:8080/network-initialization/networks/save-progress/'
    },
    NETWORK_DEBUG: {
      GET_ALL: 'http://localhost:8080/network-initialization/networks',
      GET_CONNECTIONS: 'http://localhost:8080/network/connections',
      GET_BY_ID: '',
      UPDATE: '',
      DELETE: ''
    },
    NETWORK_LOGS: {
      GET_ALL_SORTED_BY_NET_ID: 'http://localhost:8080/network-initialization/logs/'
    }
  }
};




