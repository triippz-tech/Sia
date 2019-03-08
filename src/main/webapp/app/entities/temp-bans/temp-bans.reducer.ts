import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ITempBans, defaultValue } from 'app/shared/model/temp-bans.model';

export const ACTION_TYPES = {
  FETCH_TEMPBANS_LIST: 'tempBans/FETCH_TEMPBANS_LIST',
  FETCH_TEMPBANS: 'tempBans/FETCH_TEMPBANS',
  CREATE_TEMPBANS: 'tempBans/CREATE_TEMPBANS',
  UPDATE_TEMPBANS: 'tempBans/UPDATE_TEMPBANS',
  DELETE_TEMPBANS: 'tempBans/DELETE_TEMPBANS',
  RESET: 'tempBans/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ITempBans>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type TempBansState = Readonly<typeof initialState>;

// Reducer

export default (state: TempBansState = initialState, action): TempBansState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_TEMPBANS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_TEMPBANS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_TEMPBANS):
    case REQUEST(ACTION_TYPES.UPDATE_TEMPBANS):
    case REQUEST(ACTION_TYPES.DELETE_TEMPBANS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_TEMPBANS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_TEMPBANS):
    case FAILURE(ACTION_TYPES.CREATE_TEMPBANS):
    case FAILURE(ACTION_TYPES.UPDATE_TEMPBANS):
    case FAILURE(ACTION_TYPES.DELETE_TEMPBANS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPBANS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_TEMPBANS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_TEMPBANS):
    case SUCCESS(ACTION_TYPES.UPDATE_TEMPBANS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_TEMPBANS):
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

const apiUrl = 'api/temp-bans';

// Actions

export const getEntities: ICrudGetAllAction<ITempBans> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_TEMPBANS_LIST,
  payload: axios.get<ITempBans>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<ITempBans> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_TEMPBANS,
    payload: axios.get<ITempBans>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ITempBans> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_TEMPBANS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ITempBans> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_TEMPBANS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ITempBans> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_TEMPBANS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
