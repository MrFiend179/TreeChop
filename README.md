![ALT](https://raw.githubusercontent.com/MrFiend179/TreeChop/master/treechopmnthmb.PNG)

![GitHub Downloads (all assets, all releases)](https://img.shields.io/github/downloads/MrFiend179/TreeChop/total?style=for-the-badge)
![Discord](https://img.shields.io/discord/988824696860573798?style=for-the-badge&logo=discord&color=blue)
![GitHub last commit](https://img.shields.io/github/last-commit/MrFiend179/TreeChop?style=for-the-badge&color=yellow)
![GitHub License](https://img.shields.io/github/license/MrFiend179/TreeChop?style=for-the-badge)
![GitHub Repo stars](https://img.shields.io/github/stars/MrFiend179/TreeChop?style=for-the-badge&color=gold)
![YouTube Channel Subscribers](https://img.shields.io/youtube/channel/subscribers/UCha9zIeWFX58QASlKFW3CwQ?style=for-the-badge&logo=youtube&color=red)



# Tree Chop v1.1.0

Make tree chopping rewarding in Minecraft servers with this plugin. This is a simple yet effective addition to your server that incentivizes players to engage with trees by rewarding them with in-game currency (essentialsX economy). Whether you're looking to encourage resource gathering or simply add a fun twist to gameplay, this plugin offers a seamless solution.

**Minecraft 1.18.2 - 1.20.4**

# Requirements
- [EssentialsX](https://essentialsx.net/)
-  LuckPerms (or any permissions plugin)

# Key Features:

- **Dynamic Countdown Mechanism**: The plugin tracks the number of logs in a tree and initiates a countdown when any log is broken, providing an engaging and immersive experience for players.
- **Rewarding Experience**: Upon completion of the countdown, players are rewarded for their efforts, adding incentive and satisfaction to the chopping process.
- **Immersive Animation**: Enjoy a visually captivating tree falling animation that enhances the realism and excitement of chopping down of trees.
- **Automatic Respawn**: After the tree falls, it automatically respawns after a brief 5-second interval, allowing for continuous gameplay and interaction.


# Commands

## 1 /treechopper enable
- **Description:** Enables the TreeChopper feature for spruce trees.
- **Usage:** `/treechopper enable`

## 2 /treechopper disable
- **Description:** Disables the TreeChopper feature for spruce trees.
- **Usage:** `/treechopper disable`

## 3 /treechopper forcestop
- **Description:** Stops the TreeChop plugin completely.
- **Usage:** `/treechopper forcestop`

## 4 /treechopper worldname [world_name]
- **Description:** Sets the name of the world in which plugin will work.
- **Usage:** `/treechopper worldname <world_name>`
#### 4.1 example:
- 4.1.1 `/treechopper worldname lobby` sets world in which plugin will work to **lobby**
- 4.1.2 `/treechopper worldname world` sets world in which plugin will work to **world**
- 4.1.3 `/treechopper worldname myworld` sets world in which plugin will work to **myworld**

## 5 /treechopper reward [amount]
- **Description:** Sets the amount of money Players will receive.
- **Usage:** `/treechopper reward <amount>`
#### 5.1 example:
- 5.1.1 `/treechopper reward 50` sets the reward amount player will receive to **50**
- 5.1.2 `/treechopper reward 150` sets the reward amount player will receive to **150**


## 6 /treechopper type [tree_type]
- **Description:** Sets the type of the tree the plugin will work on.
- **Usage:** `/treechopper type <tree_type>`
#### 6.1 example:

- 6.1.1 `/trechopper type oak` Sets the tree type to oak (OAK_LEAVES, OAK_LOGS)
- 6.1.2 `/trechopper type acacia` Sets the tree type to acacia (ACACIA_LEAVES, ACACIA_LOGS)


## 7 /treechopper enablelogrewards
- **Description:** Enables the Log rewarding feature which gives players logs of the tree type selected.
- **Usage:** `/treechopper enablelogrewards`

## 8 /treechopper disablelogrewards
- **Description:** Disables the Log rewarding feature which gives players logs of the tree type selected.
- **Usage:** `/treechopper disablelogrewards`

# Tree Types
The plugin suports the following mentioned tree types, this includes the whole tree custom or default


### Supported Types
- Oak
- Spruce
- Acacia
- Dark_Oak
- Birch
- Jungle

# Permission
You need to use a permissions plugin such as luckperms to handle permission otherwise it won't work.

- ```TreeChopper.ChopRewards``` Grants player access to chop trees and get rewards.
- ```TreeChopper.Command``` Grants non op players to use plugin related commands.


# How it works

- **Chop a  Tree**: Players simply chop a  tree as they would in the game.
- **Countdown Initiation**: Breaking any log in the spruce tree triggers a countdown based on the total number of logs in the tree.
- **Reward and Animation**: Upon countdown completion, players are rewarded, and a stunning tree falling animation ensues, heightening the immersive gameplay experience.
- **Automatic Respawn**: After a short delay, the spruce tree respawns, ready to be chopped again, restarting the countdown process.


# Rewards
The rewards are given when a spruce tree is broken succesfully, after which a console command is issue which gives the player money using essentialsX economy
Along with money players can also get logs as rewards of the tree type that is set.

`eco give %player% <reward_amount>`

`give %player% %tree_type_selected% %amount%`

Every player gets **$50** and **5** logs for chopping a tree succesfully by default. It can be changed via config or command.

### *Log Rewards
By default the log reward option is disabled, make sure to enable it by command and modify the log amount rewarded to players in config (default 5). 
# How to use
- Install EssentialsX and TreeChop and put them in your plugins folder
- Restart your server
- Setup EssentialsX economy however you like it
- by default the TreeChop plugin is enabled and the default world name is "Lobby"
- Everything is set if you have followed the steps

# Support
For any assistance You can join our discord server or contact me on discord ```fiend6998```.

- [Discord](https://discord.com/invite/Ga4pHSEcjK)
- [Website](https://www.flubel.com/)
