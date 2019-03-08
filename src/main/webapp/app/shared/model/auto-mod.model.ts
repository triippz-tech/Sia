export interface IAutoMod {
  id?: number;
  guildId?: number;
  resolveUrls?: boolean;
  maxMentions?: number;
  maxRoleMentions?: number;
  maxLines?: number;
  raidModeNumber?: number;
  raidModeTime?: number;
  inviteStrikes?: number;
  refStrikes?: number;
  copyPastaStrikes?: number;
  everyoneStrikes?: number;
  dupeStrikes?: number;
  dupeDeleteThresh?: number;
  dupeStrikesThresh?: number;
  dehoistChar?: number;
}

export const defaultValue: Readonly<IAutoMod> = {
  resolveUrls: false
};
