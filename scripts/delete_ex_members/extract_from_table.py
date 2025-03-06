from bs4 import BeautifulSoup
from unicodedata import normalize
import re


def normalize_text(text):
    # Define custom replacements for special characters
    replacements = {
        'ä': 'ae',
        'ö': 'oe',
        'ü': 'ue',
        'Ä': 'Ae',
        'Ö': 'Oe',
        'Ü': 'Ue',
        'ß': 'ss',
        'ç': 'c',
        'Ç': 'C'
        # Add any additional mappings as needed
    }
    # Replace special characters based on the dictionary
    for original, replacement in replacements.items():
        text = text.replace(original, replacement)
    # Remove any remaining characters that are not A-Z or a-z
#     text = re.sub(r'[^A-Za-z]', '', text)
    return text

# Suppose your HTML is stored in a file called "input.html"
with open("time_existing_people_table.html", "r", encoding="utf-8") as f:
    html_content = f.read()

soup = BeautifulSoup(html_content, 'html.parser')

# Find the table body and iterate through each row
tbody = soup.find("tbody")
with open("existing_people_time.txt", "r+", encoding="utf-8") as outfile:
    for row in tbody.find_all("tr"):
        cells = row.find_all("td")
        # Ensure that there are at least two cells for first and last names
        if len(cells) >= 2:
            first_name = cells[0].get_text(strip=True)
            last_name = cells[1].get_text(strip=True)
            person = f"{first_name},{last_name}"
            outfile.write(person + "\n")
    outfile.truncate()