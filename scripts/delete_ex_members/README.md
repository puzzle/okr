# Delete Ex-members

## Extract all Okr USERS
Run `select first_name, last_name from okr_pitc.person;` and copy the output to okr_users.txt
- One person per line in the format `first_name,last_name`

## Get all members
- Goto `https://time.puzzle.ch/employee_master_data` and copy the html of the table to `time_existing_people_table.html`

## Run the scripts
- Run `python3 extract_from_table.py` to get the list of all members in the format `first_name,last_name`
- Run `python3 find_ex_members.py` to get the list of all ex-members in the format `first_name,last_name`