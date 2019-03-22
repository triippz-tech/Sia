import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPollItems, defaultValue } from 'app/shared/model/poll-items.model';

export const ACTION_TYPES = {
  FETCH_POLLITEMS_LIST: 'pollItems/FETCH_POLLITEMS_LIST',
  FETCH_POLLITEMS: 'pollItems/FETCH_POLLITEMS',
  CREATE_POLLITEMS: 'pollItems/CREATE_POLLITEMS',
  UPDATE_POLLITEMS: 'pollItems/UPDATE_POLLITEMS',
  DELETE_POLLITEMS: 'pollItems/DELETE_POLLITEMS',
  RESET: 'pollItems/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPollItems>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PollItemsState = Readonly<typeof initialState>;

// Reducer

export default (state: PollItemsState = initialState, action): PollItemsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_POLLITEMS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_POLLITEMS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_POLLITEMS):
    case REQUEST(ACTION_TYPES.UPDATE_POLLITEMS):
    case REQUEST(ACTION_TYPES.DELETE_POLLITEMS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_POLLITEMS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_POLLITEMS):
    case FAILURE(ACTION_TYPES.CREATE_POLLITEMS):
    case FAILURE(ACTION_TYPES.UPDATE_POLLITEMS):
    case FAILURE(ACTION_TYPES.DELETE_POLLITEMS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_POLLITEMS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_POLLITEMS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_POLLITEMS):
    case SUCCESS(ACTION_TYPES.UPDATE_POLLITEMS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_POLLITEMS):
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

const apiUrl = 'api/poll-items';

// Actions

export const getEntities: ICrudGetAllAction<IPollItems> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_POLLITEMS_LIST,
  payload: axios.get<IPollItems>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPollItems> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_POLLITEMS,
    payload: axios.get<IPollItems>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPollItems> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_POLLITEMS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPollItems> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_POLLITEMS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPollItems> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_POLLITEMS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
