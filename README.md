# SQLEconomy [![Build Status](https://travis-ci.org/Renaud11232/SQLEconomy.svg?branch=master)](https://travis-ci.org/Renaud11232/SQLEconomy) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=UD54SHVYDV6NU&source=url)

A Spigot Economy system stored in SQL databases

## Dependecies

SQLEconomy needs Vault to be installed on the server in order to run properly.
With this, the plugin can easily replace any other Economy system without any compatibility issue.

## Configuration

SQLEconomy currenctly support MySQL and SQLite databases.
For each database management system, you'll need to provide database credentials/path.

This plugin will require you to know a bit about the SQL syntax in order to adapt it to your existing database scheme.

You will need to provide SQL queries to get the current balance of a user, to add and remove money from a player's account and to create a player's account.

You will also need to set your currency name (singular and plural) and symbol. (Essentials ignores this setting).

## Commands & Permissions

SQLEconomy does not provide any command for money transfer, balance management, payments...
You'll need to have another plugin for that (ie: EssentialsX).

This plugin comes with a single command :
```
/sqleconomy reload
```

This command will reread the configuration file and apply the new settings by reconnecting to the database.

Users need the `sqleconomy.reload` permission to run this command.