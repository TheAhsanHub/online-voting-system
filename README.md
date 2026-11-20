#  BallotBox

**A desktop online voting system built in Java Swing, with separate voter and admin roles.**

BallotBox lets voters register and cast a vote through a simple graphical interface, while an administrator can view registered users, see the voting records, count the total votes and reset an election. All data is stored in plain text files, so it runs anywhere Java is installed with no database needed.

##  Features

**Voter**
- Log in with name and email (with basic validation)
- Register by entering age and gender
- Choose a candidate and vote. Voting only unlocks after registration, and each session can vote once

**Admin**
- Secure login screen
- View all registered users
- See who voted for whom
- Show the total number of votes cast
- Reset all votes to start a new election

##  Tech stack

- **Java** (Swing GUI with `JFrame`, layout managers and event listeners)
- **File I/O** for persistent storage (`user_profiles.txt`, `votes.txt`)
- Object-oriented design: one class per screen (`MainMenu`, `UserLogin`, `AdminLogin`, `UserPanel`, `AdminPanel`)

##  Getting started

You need Java 8 or newer.

    git clone https://github.com/TheAhsanHub/online-voting-system.git
    cd online-voting-system
    javac OnlineVotingSystem.java
    java OnlineVotingSystem

You can also open the folder in IntelliJ IDEA and run `OnlineVotingSystem`.

**Demo admin login:** ID `admin` / password `admin123`

##  Limitations and future improvements

This was built as a learning project, so some parts are deliberately simple:

- Admin credentials are hard-coded. Next step: store hashed passwords.
- Votes are stored in text files. Next step: move to a database such as MySQL or SQLite.
- Duplicate voting is only prevented within one session. Next step: check against registered emails.
- Candidate names are fixed in the code. Next step: let the admin manage candidates.

## Author

**Ahsan**, BSc (Hons) Computer Science, Cardiff Metropolitan University
