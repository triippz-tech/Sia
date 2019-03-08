import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IActions, defaultValue } from 'app/shared/model/actions.model';

export const ACTION_TYPES = {
  FETCH_ACTIONS_LIST: 'actions/FETCH_ACTIONS_LIST',
  FETCH_ACTIONS: 'actions/FETCH_ACTIONS',
  CREATE_ACTIONS: 'actions/CREATE_ACTIONS',
  UPDATE_ACTIONS: 'actions/UPDATE_ACTIONS',
  DELETE_ACTIONS: 'actions/DELETE_ACTIONS',
  RESET: 'actions/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IActions>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ActionsState = Readonly<typeof initialState>;

// Reducer

export default (state: ActionsState = initialState, action): ActionsState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_ACTIONS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_ACTIONS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_ACTIONS):
    case REQUEST(ACTION_TYPES.UPDATE_ACTIONS):
    case REQUEST(ACTION_TYPES.DELETE_ACTIONS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_ACTIONS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_ACTIONS):
    case FAILURE(ACTION_TYPES.CREATE_ACTIONS):
    case FAILURE(ACTION_TYPES.UPDATE_ACTIONS):
    case FAILURE(ACTION_TYPES.DELETE_ACTIONS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIONS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_ACTIONS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_ACTIONS):
    case SUCCESS(ACTION_TYPES.UPDATE_ACTIONS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_ACTIONS):
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

const apiUrl = 'api/actions';

// Actions

export const getEntities: ICrudGetAllAction<IActions> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_ACTIONS_LIST,
  payload: axios.get<IActions>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IActions> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_ACTIONS,
    payload: axios.get<IActions>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IActions> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_ACTIONS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IActions> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_ACTIONS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IActions> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_ACTIONS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
