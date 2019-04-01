/*
 * Copyright 2018 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.trievosoftware.discord;

import net.dv8tion.jda.core.Permission;

import java.time.OffsetDateTime;

/**
 *
 * @author John Grosh (jagrosh)
 */
public class Constants
{
    public final static OffsetDateTime STARTUP = OffsetDateTime.now();
    public final static String PREFIX          = "/";
    public final static String SUCCESS         = "<a:success:547954162562760745>";
    public final static String WARNING         = "<:Warning:547954125409615873>";
    public final static String ERROR           = "<:Error:547954145932476458>";
    public final static String LOADING         = "<a:typing:553204321383350283>";
    public final static String HELP_REACTION   = SUCCESS.replaceAll("<a?:(.+):(\\d+)>", "$1:$2");
    public final static String ERROR_REACTION  = ERROR.replaceAll("<a?:(.+):(\\d+)>", "$1:$2");
    public final static String SIA_EMOJII      = "<:sia:553207744673349650>";
    public final static Permission[] PERMISSIONS = {Permission.ADMINISTRATOR, Permission.BAN_MEMBERS, Permission.KICK_MEMBERS, Permission.MANAGE_ROLES,
                                        Permission.MANAGE_SERVER, Permission.MESSAGE_ADD_REACTION, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_READ,
                                        Permission.MESSAGE_WRITE,Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_HISTORY, Permission.MESSAGE_EXT_EMOJI,
                                        Permission.MESSAGE_MANAGE, Permission.VOICE_CONNECT, Permission.VOICE_MOVE_OTHERS, Permission.VOICE_DEAF_OTHERS, 
                                        Permission.VOICE_MUTE_OTHERS, Permission.NICKNAME_CHANGE, Permission.NICKNAME_MANAGE, Permission.VIEW_AUDIT_LOGS};
    public final static String SERVER_INVITE = "https://discord.gg/sKjSa8X";
    //public final static String BOT_INVITE  = "https://discordapp.com/oauth2/authorize?client_id=240254129333731328&scope=bot&permissions="+Permission.getRaw(PERMISSIONS);
    // public final static String BOT_INVITE    = "https://discordapp.com/oauth2/authorize?client_id=169463754382114816&scope=bot&permissions="+Permission.getRaw(PERMISSIONS);
    public final static String OWNER_ID      = "110926644876689408";
    public final static String DONATION_LINK = "https://paypal.me/marktripoli";
    public final static String XLM_DONATION_ADDR = "GDHX7GSK6T2VPUHECJERKW7PUAZ3X3NHIPFLJSRXZ6IZYNMO3FHHTBAT";
    public final static String NEED_PRO      = WARNING + " Sorry, this feature requires Sia Pro. Sia Pro is not available yet.";
    public final static String SEARCHING = "\uD83D\uDD0E"; //🔎
    public final static String PARTY_EMOJI = "\uD83C\uDF89"; //🎉
    public final static String[] NUMBERS = new String[]{"1\u20E3","2\u20E3","3\u20E3",
        "4\u20E3","5\u20E3","6\u20E3","7\u20E3","8\u20E3","9\u20E3", "\uD83D\uDD1F"};
    public final static String POLL_EMOJI = "\uD83D\uDCCA"; //📊
    
    public final static class Wiki
    {
        public final static String SHORT_WIKI     = "https://git.io/fhpP2";
        public final static String SHORT_COMMANDS = "https://git.io/fhpPa";
        
        public final static String WIKI_BASE    = "https://github.com/Trievo/Sia/wiki";
        public final static String START        = WIKI_BASE + "/Getting-Started";
        public final static String LOG_TIMEZONE = WIKI_BASE + "/Log-Timezone";
        public final static String RAID_MODE    = WIKI_BASE + "/Raid-Mode";
        public final static String COMMANDS     = WIKI_BASE + "/Commands";
        public final static String AUTOMOD      = WIKI_BASE + "/Auto-Moderation";
    }

    public final static class Music
    {
        public final static String PLAY_EMOJI  = "\u25B6"; // ▶
        public final static String PAUSE_EMOJI = "\u23F8"; // ⏸
        public final static String STOP_EMOJI  = "\u23F9"; // ⏹
        public final static Permission[] RECOMMENDED_PERMS = new Permission[]{Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY, Permission.MESSAGE_ADD_REACTION,
            Permission.MESSAGE_EMBED_LINKS, Permission.MESSAGE_ATTACH_FILES, Permission.MESSAGE_MANAGE, Permission.MESSAGE_EXT_EMOJI,
            Permission.MANAGE_CHANNEL, Permission.VOICE_CONNECT, Permission.VOICE_SPEAK, Permission.NICKNAME_CHANGE};
    }


    public final static String MARKDOWN =
        "```fix\n" +
            "#---------------> DISCORD TEXT FORMATTING <---------------#\n" +
            "        This is a guide to easy formatting in discord\n" +
            "You can use this formatting guide for saving text with the Sia Bot\n" +
            "```\n" +
            "__***Images and Links:***__\n" +
            "```\n" +
            "    Links = [My Link](www.mylink.com)\n" +
            "    Images = \n" +
            "```\n" +
            "__***outside a code block:***__\n" +
            " \n" +
            "```\n" +
            "italics = *italics*\n" +
            "bold  = **bold**\n" +
            "bold italics = ***bold italics***\n" +
            "strikeout = ~~strikeout~~\n" +
            "underline = __underline__\n" +
            "underline italics = __*underline italics*__\n" +
            "underline bold = __**underline bold**__\n" +
            "underline bold italics = __***underline bold italics***__\n" +
            "code block =  ``text here`` (3 backticks --> alt + 7 for me)\n" +
            "```\n" +
            " \n" +
            "___***code block formating:***___\n" +
            " \n" +
            "```java\n" +
            " To enable code highlighting:\n" +
            "    (three backticks `) java/c/c++/python/etc\n" +
            "        your code\n" +
            "    (three backticks `)\n" +
            "```\n" +
            "\n" +
            " ```c\n" +
            "#include <stdio.h>\n" +
            "int addNumbers(int n);\n" +
            "\n" +
            "int main()\n" +
            "{\n" +
            "    int num;\n" +
            "    printf(\"Enter a positive integer: \");\n" +
            "    scanf(\"%d\", &num);\n" +
            "    printf(\"Sum = %d\",addNumbers(num));\n" +
            "    return 0;\n" +
            "}\n" +
            "\n" +
            "int addNumbers(int n)\n" +
            "{\n" +
            "    if(n != 0)\n" +
            "        return n + addNumbers(n-1);\n" +
            "    else\n" +
            "        return n;\n" +
            "}\n" +
            " ```\n" +
            " \n" +
            "```md\n" +
            " \n" +
            "(3 backticks)markdown\n" +
            "or just:\n" +
            "(3 backticks)md\n" +
            " \n" +
            "#lines starting with # are blue(?),\n" +
            "[murky blue][red?] --------------------> anywhere in the code block\n" +
            "[murky blue](red?) --------------------> anywhere in the code block\n" +
            "<first_word_blue and the rest orange> -> anywhere in the code block\n" +
            "(3 backticks)\n" +
            "```\n" +
            " \n" +
            "```diff\n" +
            "(3 backticks)diff uses the first character on a line for color:\n" +
            " \n" +
            "+ for green,\n" +
            "- for red,\n" +
            "--- for grey\n" +
            "! for green(? should be orange)\n" +
            "(3 backticks)\n" +
            "```\n" +
            " \n" +
            "```css\n" +
            " \n" +
            "(3 backticks)css makes almost everything green,\n" +
            "numbers are white (0123456789),\n" +
            "has some weird cases around some specific characters like colons and quotes\n" +
            "(3 backticks)\n" +
            "```\n" +
            " \n" +
            "```fix\n" +
            "(3 backticks)fix makes everything orange\n" +
            "(3 backticks)\n" +
            "```\n";
}
