import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStrikes, defaultValue } from 'app/shared/model/strikes.model';

export const ACTION_TYPES = {
  FETCH_STRIKES_LIST: 'strikes/FETCH_STRIKES_LIST',
  FETCH_STRIKES: 'strikes/FETCH_STRIKES',
  CREATE_STRIKES: 'strikes/CREATE_STRIKES',
  UPDATE_STRIKES: 'strikes/UPDATE_STRIKES',
  DELETE_STRIKES: 'strikes/DELETE_STRIKES',
  RESET: 'strikes/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStrikes>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type StrikesState = Readonly<typeof initialState>;

// Reducer

export default (state: StrikesState = initialState, action): StrikesState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_STRIKES_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STRIKES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STRIKES):
    case REQUEST(ACTION_TYPES.UPDATE_STRIKES):
    case REQUEST(ACTION_TYPES.DELETE_STRIKES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_STRIKES_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STRIKES):
    case FAILURE(ACTION_TYPES.CREATE_STRIKES):
    case FAILURE(ACTION_TYPES.UPDATE_STRIKES):
    case FAILURE(ACTION_TYPES.DELETE_STRIKES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_STRIKES_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STRIKES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STRIKES):
    case SUCCESS(ACTION_TYPES.UPDATE_STRIKES):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STRIKES):
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

const apiUrl = 'api/strikes';

// Actions

export const getEntities: ICrudGetAllAction<IStrikes> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_STRIKES_LIST,
  payload: axios.get<IStrikes>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IStrikes> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STRIKES,
    payload: axios.get<IStrikes>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStrikes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STRIKES,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStrikes> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STRIKES,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStrikes> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STRIKES,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
