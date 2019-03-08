export interface IAuditCache {
  id?: number;
  guildId?: number;
  old?: number;
  older?: number;
  oldest?: number;
}

export const defaultValue: Readonly<IAuditCache> = {};
