from setuptools import setup, find_packages

setup(
    name='ailayoutserverapp',
    version='0.1',
    packages=find_packages(),
    include_package_data=True,
    install_requires=[
        'flask',
        'openai',
    ],
    entry_points={
        'console_scripts': [
            'ailayoutserverapp = main:main',
        ],
    },
)