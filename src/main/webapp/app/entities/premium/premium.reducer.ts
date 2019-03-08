import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPremium, defaultValue } from 'app/shared/model/premium.model';

export const ACTION_TYPES = {
  FETCH_PREMIUM_LIST: 'premium/FETCH_PREMIUM_LIST',
  FETCH_PREMIUM: 'premium/FETCH_PREMIUM',
  CREATE_PREMIUM: 'premium/CREATE_PREMIUM',
  UPDATE_PREMIUM: 'premium/UPDATE_PREMIUM',
  DELETE_PREMIUM: 'premium/DELETE_PREMIUM',
  RESET: 'premium/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPremium>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PremiumState = Readonly<typeof initialState>;

// Reducer

export default (state: PremiumState = initialState, action): PremiumState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PREMIUM_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PREMIUM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PREMIUM):
    case REQUEST(ACTION_TYPES.UPDATE_PREMIUM):
    case REQUEST(ACTION_TYPES.DELETE_PREMIUM):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PREMIUM_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PREMIUM):
    case FAILURE(ACTION_TYPES.CREATE_PREMIUM):
    case FAILURE(ACTION_TYPES.UPDATE_PREMIUM):
    case FAILURE(ACTION_TYPES.DELETE_PREMIUM):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PREMIUM_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PREMIUM):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PREMIUM):
    case SUCCESS(ACTION_TYPES.UPDATE_PREMIUM):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PREMIUM):
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

const apiUrl = 'api/premiums';

// Actions

export const getEntities: ICrudGetAllAction<IPremium> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PREMIUM_LIST,
  payload: axios.get<IPremium>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPremium> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PREMIUM,
    payload: axios.get<IPremium>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPremium> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PREMIUM,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPremium> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PREMIUM,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPremium> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PREMIUM,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
