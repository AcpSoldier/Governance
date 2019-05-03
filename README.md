# Government Project
Project Govern



Commands

Commands are the main way for players and admins to interact with the plugin. The master/parents command is /government or /gov.

Command
Usage
Description
/gov
N/A
All commands stem from this one. Displays list of commands when typed by a player.
/gov respect
/gov respect | /gov respect <user>
Shows the Respect Level of the command sender or the specified user.
/gov propose 
/gov propose <vote type> <description / reason>
Announces a public proposal (with description) for other users to consider.
/gov vote 
/gov vote <yes | no >
Allows the user to vote on the current proposal.
/gov nominate
/gov nominate <player> <position>


/gov run
/gov run <position>
Allows the user to run for a government position.
/gov elect
/gov elect <user> <position>
Allows the user to cast their vote towards electing a public official. 
/gov config
/gov config <setting> <value>
Allows server owners to change the plugin settings.


 
Proposals
Overview
A proposal is a basic vote on any given action. A proposal will be considered if all of the requirements are met:
There are currently no proposals “on the table”.
The minimum amount of online players has been met.
The proposer meets the minimum Respect Level for the proposal type.
If a user proposes a vote that succeeds, their Respect Level will be adjusted. If the proposal fails, that proposal type may not be made again until the “repeat proposal” time expires. 
 
Types of proposals 
The default proposals are based on the vanilla Minecraft commands. A user may use the “propose” to propose a vote on any of the following actions.
Proposal Type
Description
SetTime
Changes the time of the server. The proposer may specify a numerical value (ex, 6000 = day), or a simply use “day” or “night”.
SetWeather
Changes the weather of the server. The proposer may specify one of the three options: “clear”, “rain”, or “thunder”.
Kick
Kicks a player from the server.
GameRule
Changes the specified gamerule of the server.
<CUSTOM>
Runs any other plugin’s (3rd party) command.

 
Announcements
When a users enters the “proposal” command, their proposal is first announced in chat. Included in the announcement is the proposers current Respect Level, the amount of time until it expires, and what the actual proposal is. If half of the “expired time” has passed and the proposal has not passed, another announcement will be made. If a proposal does not pass within the amount of time (defined in the plugin configuration), another announcement will be made that the proposal expired and therefore failed.

Configurations
Overview
Configurations are certain options or values that can be adjusted by the server owner (or potentially a government official / player-base). All of the settings below may be found and edited in the plugin’s config.yml file.
Configuration Name 
Description
Settings.Enabled
Whether the plugin is enabled or not.
Settings.AutoUpdate
If set to true, server operators (and the server console) will be notified when a new version of the plugin is available.
Proposals.RepeatTime
The amount of time (in server ticks) that a recently failed proposal may be proposed again.
Proposals.ExpireTime


Proposals.MinimumOnlinePlayers
The amount of players the must be alone for proposals to be considered.
Proposals.MajorityPercent
The percentage of players that must agree to a proposal in order for it to pass.
Proposals.Command.<Name>
Defines a 3rd party command (to be run by the console) that players can propose votes on.
Proposals.Command.<Name>.MinimumRespect
The minimum Respect Level required for a player to run a command. 
Elections.MaxTermLength
How long until an elected officials term is. (AKA when it expires)
Elections.MinTermLength
How long until a newly elected official can be replaced or impeached.
Elections.Republic.MaxInOffice
The amount of players that can be in office at the same time.
Elections.RunDelayTime
The amount of a time (in ticks) a player must wait until they can run for office again.
Respect Level
Overview
A Respect Level is a numerical value assigned to every player that proposes a vote or runs for office. This value is included in the announcement of every proposal or election. Depending on the plugin configuration, users with higher the minimum required Respect Level may be able to run for office or propose special votes. Additionally, a player’s Respect Level can be used to determine whether or not a player is deemed trustworthy by the community, helping new players decide on how to vote.


Voting
Overview
Users can click on the message in the chat and vote using the chest interface, or by typing the “vote” command and specifying “yes” or “no”. Included in the announcement is the proposers current Respect Level.

For a vote to pass, the amount of “yes” votes must meet the minimum required number/percentage that is defined in the plugin configuration. The minimum votes If a proposal does not meet the required number of votes by the time

Elections
Overview
A player can be elected to a government position in one of two ways:
They are nominated by another player (using the “nominate” command).
They decide to run for office themselves. (using the “run” command).
When a player is either nominated or decides to run, an election begins immediately. If the elected player loses, they will not be able to run for office or be nominated again until the “RunDelayTime” (defined in configuration) has expired. When an election is won, the winner takes power and remains in office until one of the following happens:
Their term ends (role expires)
They are replaced by another player.
When a player is first elected into a position, they can not be replaced until the “MinTermLength” time has expired. If a government official is not challenged, they will remain in office until the “MaxTermLength” time has expired. Unlike, being replaced, when a term expires, that player may run for office again immediately. 

Government Types
Overview
The government system is what controls how proposals are decided on. There are three different proposal settings (AKA: government types).

Republic
In a republic, only government officials, who are elected by the public, can vote on proposals. There can only be a certain amount of government officials in office at once. If the maximum amount of officials has not been reached, there will be no competition. However, if the maximum amount of officials has already been reached, players must run against one of the players already in office.

Participatory Democracy 
A participatory democracy is a mix between a Direct Democracy and a Republic. There are two main configurations that make this government type useful:
Option to give elected officials more voting power than regular players.
Option to restrict the creation of proposals only to elected officials.

Dictatorship
In a dictatorship, players vote on a single person to make all decisions. When a dictator makes a proposal, it is taken into action immediately without any voting. Dictators can 

Direct Democracy
In a complete democracy, there are no elections since every player is directly involved with the decisions. When players propose votes that succeed, the proposer increases their Respect Level.


Impeachment & Resignations
Overview
At all times, any given elected official has the option to be resign from office. If a player resigns, they can not be nominated or run for office until the “RunDelayTime” expires. When a resignation takes place, a public announcement is made in chat including a message from the resignee. 

If the players of a server are unhappy with an elected officials, they can attempt to impeach them. This is only possible when the “MinTermLength” has expired. 
