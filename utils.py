import os
from datetime import datetime
import uuid


current_dir = os.getcwd()
cache_dir = os.path.join(current_dir, 'cache')
RECORD_CACHE_DIR = cache_dir
if not os.path.exists(RECORD_CACHE_DIR):
    os.makedirs(RECORD_CACHE_DIR)


def gen_file_name():
    now = datetime.now()
    timestamp = now.strftime("%Y%m%d_%H%M%S")  # e.g. "20250413_142300"
    filename = f"ai_generated_{timestamp}.html"
    print("gen_file_name:", filename)
    return filename


def save_latest_html(content="<html><body><h1>Hello, AI!</h1></body></html>", file_name=None):
    if file_name is None:
        file_name = gen_file_name()
    if content:
        target_filepath = os.path.join(RECORD_CACHE_DIR, file_name)
        with open(target_filepath, 'w', encoding='utf-8') as file:
            file.write(content)


def read_html(filename):
    path = f"./templates/{filename}"
    with open(path, "r", encoding="utf-8") as f:
        content = f.read(-1)
    return content


def save_generated_html(content: str) -> str:
    now = datetime.now()
    filename = f"ai_generated_{now.strftime('%Y%m%d_%H%M%S')}.html"
    path = f"./templates/{filename}"
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    return filename


def gen_uuid():
    return str(uuid.uuid4()).split("-")[1]


if __name__ == '__main__':
    content = read_html(filename="ai_generated_20250414_191432.html")
    print(content)