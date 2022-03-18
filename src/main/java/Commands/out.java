package Commands;

import Functions.DatabaseHandles;
import Functions.DatabaseParameters;
import Functions.TimeThread;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class out extends Command {
    public out(){
        this.name = "out";
        this.aliases = new String []{"logout", "bounce", "signout"};
        this.help = "logs you out!";
        this.guildOnly = true;
        this.hidden = false;
    }

    @Override
    protected void execute(CommandEvent e){
        DatabaseHandles io = new DatabaseHandles();
        String ID = e.getAuthor().getId();

        // Not part of team
        if(io.findUser(e.getAuthor().getId()).equals("")){
            e.replyInDm("You are not allowed to do that!");
            if(!e.getMessage().getTextChannel().getId().equals(DatabaseParameters.getChannelID())){
                e.getMessage().delete().queue();
            }
            return;
        }

        // Not in the designated channel
        if(!e.getMessage().getTextChannel().getId().equals(DatabaseParameters.getChannelID())){
            e.getMessage().delete().queue();
            e.getJDA().getTextChannelById(DatabaseParameters.getChannelID()).sendMessage("Do that here! " + e.getAuthor().getAsMention()).queue();
            return;
        }

        // not logged on
        if(!io.actionEligibility(ID)[0]){
            e.reply("You're not even logged on lol " + e.getAuthor().getAsMention());
            return;
        }

        // Not on Break
        if(io.actionEligibility(ID)[1]){
            e.reply("You're still on break and you wanna log out now " + e.getAuthor().getAsMention() +"?? the audacity...");
            return;
        }
        io.writeActivity(TimeThread.getDate(), TimeThread.getTime(), io.findUser(ID), "Logged Out", "");
        io.updateEligibility(ID, 'A');
        e.reply(io.findUser(ID) + " logged out: " + TimeThread.getDate() + " - " + TimeThread.getTime());
    }
}
