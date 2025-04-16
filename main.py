import os
import glob
import claude_bot
from flask import Flask, render_template, jsonify, make_response, send_from_directory, request

import utils

app = Flask(__name__)
HTML_FOLDER = "./templates/"
last_filename = 'default.html'  # 缓存上次返回的文件名


@app.route("/favicon.ico")
def favicon():
    from flask import Response
    return Response(status=204)


@app.route("/check_update")
def check_update():
    global last_filename
    try:
        html_files = glob.glob(os.path.join(HTML_FOLDER, "*.html"))
        print(html_files)
        if not html_files:
            return jsonify(update=False)
        # 找到最近修改的文件
        latest_file = max(html_files, key=os.path.getmtime)
        latest_filename = os.path.basename(latest_file)
        print(latest_filename)
        if latest_filename != last_filename:
            last_filename = latest_filename
            return jsonify(update=True, filename=latest_filename)
        else:
            return jsonify(update=False, filename=last_filename)
    except Exception as e:
        return jsonify(update=False, error=str(e))


@app.route('/<path:filename>')
def get_layout(filename: str):
    response = make_response(send_from_directory("templates", filename))
    return response


@app.route('/')
@app.route('/home')
@app.route('/index')
@app.route('/admin')
def home():
    return render_template('default.html')


@app.route('/edit')
def edit():
    return render_template('edit_promote.html')


@app.route('/request_ai', methods=['POST'])
def request_ai():
    data = request.get_json()
    user_promote = data.get('userText', '')
    print(f"{data}")
    html_content = claude_bot.gen_layout(promote=user_promote, html_file=utils.read_html(last_filename))
    utils.save_generated_html(html_content)
    response = {
        'status': 'success',
    }
    return jsonify(response), 200


if __name__ == '__main__':
    # Import Android-specific permissions if available
    try:
        from android.permissions import request_permissions, Permission
        request_permissions([Permission.INTERNET])
    except ImportError:
        pass
    app.run(host="0.0.0.0", port=7749)
