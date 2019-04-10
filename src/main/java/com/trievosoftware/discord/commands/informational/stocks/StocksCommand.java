package com.trievosoftware.discord.commands.informational.stocks;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.trievosoftware.discord.Sia;
import com.trievosoftware.discord.commands.meta.AbstractGenericCommand;
import net.dv8tion.jda.core.entities.Message;

import java.util.List;

@SuppressWarnings("Duplicates")
public class StocksCommand extends AbstractGenericCommand {

    public StocksCommand(Sia sia) {
        super(sia);
        this.name = "stocks";
        this.aliases = new String[]{"stock"};
        this.help = "Displays relevant stock market information";
        this.arguments = "<price>|<info>|<news>";
        this.children = new Command[]
            {
                new StockPriceCommand(sia),
                new StockInfoCommand(sia),
                new StockNewsCommand(sia)
            };
    }

    @Override
    public void doCommand(CommandEvent event)
    {
        StringBuilder builder = new StringBuilder(event.getClient().getWarning() + " Guild Poll Commands:\n");
        for (Command cmd : this.children)
            builder.append("\n`").append(event.getClient().getPrefix()).append(name).append(" ").append(cmd.getName())
                .append(" ").append(cmd.getArguments() == null ? "" : cmd.getArguments()).append("` - ").append(cmd.getHelp());
        event.reply(builder.toString());
    }

    public class StockPriceCommand extends AbstractGenericCommand
    {
        StockPriceCommand(Sia sia) {
            super(sia);
            this.name = "price";
            this.help = "Displays price information for a stock";
            this.arguments = "<ticker symbol>";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            if ( event.getArgs().isEmpty() )
            {
                event.replyError("Must include a ticker symbol. Ex. `AAPL` ");
                return;
            }
            event.reply("Standby, let me see what I can find . . .");
            event.reply(this.sia.getServiceManagers().getIexService().getQuote(event.getArgs().trim()));
        }
    }

    public class StockInfoCommand extends AbstractGenericCommand
    {
        StockInfoCommand(Sia sia)
        {
            super(sia);
            this.name = "info";
            this.help = "Displays company related information";
            this.arguments = "<ticker symbol>";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            if ( event.getArgs().isEmpty() )
            {
                event.replyError("Must include a ticker symbol. Ex. `AAPL` ");
                return;
            }
            event.reply("Standby, let me see what I can find . . .");
            event.reply(this.sia.getServiceManagers().getIexService().getInfo(event.getArgs().trim()));
        }
    }

    public class StockNewsCommand extends AbstractGenericCommand
    {
        StockNewsCommand(Sia sia)
        {
            super(sia);
            this.name = "news";
            this.help = "Displays company related news";
            this.arguments = "<ticker symbol>";
        }

        @Override
        public void doCommand(CommandEvent event)
        {
            if ( event.getArgs().isEmpty() )
            {
                event.replyError("Must include a ticker symbol. Ex. `AAPL` ");
                return;
            }
            event.reply("Standby, let me see what I can find . . .");
            List<Message> messages = sia.getServiceManagers().getIexService().getNews(event.getArgs().trim());

            for ( int i = 0; i < 3 ; i++ )
                event.reply(messages.get(i));
        }
    }
}
