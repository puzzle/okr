with open("ex_members.txt", "r", encoding="utf-8") as file:
    lines = file.readlines()

    # Build a list of tuples for first_name and last_name
    names = []
    for line in lines:
        line = line.strip()
        if not line:
            continue
        # Split the line into first and last name
        parts = line.split(',')
        if len(parts) < 2:
            continue
        first_name = parts[0].strip().replace("'", "''")  # Escape single quotes for SQL
        last_name = parts[1].strip().replace("'", "''")
        names.append((first_name, last_name))

    # Generate the SQL query dynamically
    values_str = ",\n    ".join(f"('{first}', '{last}')" for first, last in names)
    sql_query = f"DELETE FROM person\nWHERE (first_name, last_name) IN (\n    {values_str}\n);"

    # Print the query to the terminal
    print(sql_query)