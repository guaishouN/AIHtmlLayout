import re

from openai import OpenAI

client = OpenAI(api_key="sk-1XW4yJnBrCL00UQg22BfAf2e14Eb492c83E436BdAc2b1499", base_url="https://api.laozhang.ai/v1")


def remove_markdown_code_block(md_code: str) -> str:
    """
    使用正则表达式移除Markdown代码块中的语言标记（如 ```python 或 ```bash），保留代码内容。

    :param md_code: 含有Markdown代码块标记的代码
    :return: 去掉代码块标记后的代码
    """
    # 正则表达式匹配以 ``` 开头的代码块，不指定具体语言
    pattern = r"```[a-zA-Z]*\n(.*?)```"
    match = re.search(pattern, md_code, re.DOTALL)

    if match:
        return match.group(1)
    return md_code


def gen_layout(promote="", html_file=None):
    response = client.chat.completions.create(
        model="claude-3-7-sonnet-20250219",
        messages=[
            {"role": "system", "content": "你是HTML布局服务器，能根据用户需求生成直接给浏览器加载的html文件。"
                                          "现在你的客户是车载行业Android客户端产品经理，他希望快速调整UI布局，展示他的车载应用软件产品布局效果。"
                                          "你要根据这个产品经理的提示快速生成单个html布局文件，以便他使用浏览器打开。"
                                          "你采用的html UI框架是framework7-bundle,"
                                          "并且framework7-icons.min.css和framework7-bundle.min.js已经配置到static文件夹下了。"
                                          "图片和图标使用img.icons8.com的icon和图片;你有更稳定连接更快的图片图标也使用。"
                                          "注意："
                                          "1) 你需要做的是直接返回html文件内容。"
                                          "2) 如果用户提供的Html文件不为空，请根据用户的需求修改界面html; 所以你要理解用户的意图是全新设计还是方案修改"
                                          "3) 如果用户没有指定主题，那就使用浅色主题"
             },
            {"role": "user", "content": "我提供的html="+html_file},
            {"role": "user", "content": "你作为智能HTML布局服务, "+promote+", 请输出html文件"},
        ],
        stream=False
    )
    layout_content = response.choices[0].message.content
    print(layout_content)
    return remove_markdown_code_block(layout_content)


if __name__ == '__main__':
    gen_layout(promote="生成一个网格型的carsetting布局")
