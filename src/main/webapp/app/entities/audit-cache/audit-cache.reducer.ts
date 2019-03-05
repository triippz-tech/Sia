import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAuditCache, defaultValue } from 'app/shared/model/audit-cache.model';

export const ACTION_TYPES = {
  FETCH_AUDITCACHE_LIST: 'auditCache/FETCH_AUDITCACHE_LIST',
  FETCH_AUDITCACHE: 'auditCache/FETCH_AUDITCACHE',
  CREATE_AUDITCACHE: 'auditCache/CREATE_AUDITCACHE',
  UPDATE_AUDITCACHE: 'auditCache/UPDATE_AUDITCACHE',
  DELETE_AUDITCACHE: 'auditCache/DELETE_AUDITCACHE',
  RESET: 'auditCache/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAuditCache>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type AuditCacheState = Readonly<typeof initialState>;

// Reducer

export default (state: AuditCacheState = initialState, action): AuditCacheState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_AUDITCACHE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_AUDITCACHE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_AUDITCACHE):
    case REQUEST(ACTION_TYPES.UPDATE_AUDITCACHE):
    case REQUEST(ACTION_TYPES.DELETE_AUDITCACHE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_AUDITCACHE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_AUDITCACHE):
    case FAILURE(ACTION_TYPES.CREATE_AUDITCACHE):
    case FAILURE(ACTION_TYPES.UPDATE_AUDITCACHE):
    case FAILURE(ACTION_TYPES.DELETE_AUDITCACHE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUDITCACHE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_AUDITCACHE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_AUDITCACHE):
    case SUCCESS(ACTION_TYPES.UPDATE_AUDITCACHE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_AUDITCACHE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/audit-caches';

// Actions

export const getEntities: ICrudGetAllAction<IAuditCache> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_AUDITCACHE_LIST,
  payload: axios.get<IAuditCache>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IAuditCache> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_AUDITCACHE,
    payload: axios.get<IAuditCache>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAuditCache> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_AUDITCACHE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAuditCache> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_AUDITCACHE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAuditCache> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_AUDITCACHE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
