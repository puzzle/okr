# Read names from output.txt and store them in a set for fast lookup
with open('existing_people_time.txt', 'r', encoding='utf-8') as f:
    all_people_time = {line.strip().lower() for line in f if line.strip()}

# Initialize a list to hold names not found in output.txt
ex_members = []

# Read names from okr_people.txt and check if they are in the extracted_names set
with open('okr_users.txt', 'r', encoding='utf-8') as f:
    for line in f:
        person = line.strip()
        if person and person.lower() not in all_people_time:
            ex_members.append(person)

# Write the names not found into left_people.txt, one person per line
with open('ex_members.txt', 'r+', encoding='utf-8') as f:
    for person in ex_members:
        f.write(person + "\n")
    f.truncate()
