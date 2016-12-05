To compile the java files of the project just run make.
The data items will start off uninitialized

to start the project run 'java RunProject #' where # is an optional parameter specifying the number of servers to start up (1-10)
e.g. 'java RunProject 4'

Then you will be requested to enter your Qr and Qw values

The program will then run through the transactions in trans.txt and you will see the resulting data files and log files
outputted in a seperate directory for each server. The directories are labelled after the ports that the servers are using.
These are ports 9030-9039

Finally you can edit the ip address that the servers will run on in the config.yml file. It's default is localhost
