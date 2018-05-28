#!/usr/bin/env python3
# -*- coding: utf-8 -*-
from flask import Flask
from flask import request
import json
import pymysql
import pdb
import uuid
import time
import threading


app = Flask(__name__)

def check_token(username,token):
    if(not token):
        return False
    db = pymysql.connect("localhost","root","123456","chat" )
    cursor = db.cursor()
    cursor.execute("SELECT username FROM user WHERE username='%s' AND token='%s' AND tokenexpires > %s LIMIT 1" % (username,token,int(time.time()*1000)))
    result = cursor.fetchone()
    if(result):
        return result
    else:
        return False






@app.route('/login', methods=['POST'])
def login():
    username = request.form.get('username') or ''
    password = request.form.get('password') or ''
    token = request.form.get('token') or ''
  
    db = pymysql.connect("localhost","root","123456","chat" )
    cursor = db.cursor()
    if(token):
        cursor.execute("SELECT COUNT(id) FROM user WHERE username='%s' AND token='%s' AND tokenexpires > %s LIMIT 1" % (username,token,int(time.time()*1000)))
        
    else:
        cursor.execute("SELECT COUNT(id) FROM user WHERE username='%s' AND password='%s' LIMIT 1" % (username,password))

    data = cursor.fetchone()

    # cursor.close()
    # db.close()
    # pdb.set_trace()
    if(token):
        if(data[0] > 0):
            result = {'errcode':0,'msg':'登录成功！'}

        else:
            result = {'errcode':2,'msg':'登录已过期！'}
            cursor.close()
            db.close()

        return json.dumps(result)
    else:
        if(data[0] > 0):
            token = str(uuid.uuid3(uuid.NAMESPACE_DNS,username))
            cursor.execute("UPDATE user SET token='%s',tokenexpires='%s',lastlogintime=thislogintime,thislogintime='%s' WHERE username='%s' LIMIT 1" % (token,int(time.time() * 1000) + (86400 * 1000 * 30),int(time.time()*1000),username))
            db.commit()
            result = {'errcode':0,'msg':'登录成功！','token':token}
            

        else:
            result = {'errcode':1,'msg':'用户名或密码错误！'}
        
        cursor.close()
        db.close()
        return json.dumps(result)





@app.route('/register',methods=['POST'])
def register():
    username = request.form.get('username') or ''
    password = request.form.get('password') or ''

    db = pymysql.connect("localhost","root","123456","chat" )
    cursor = db.cursor()
    cursor.execute("SELECT COUNT(id) FROM user WHERE username='%s' LIMIT 1"  % (username))
    if(cursor.fetchone()[0] != 0):
        return json.dumps({'errcode':1,'msg':'用户名已存在！'})

    sql = "INSERT INTO user(username,password,regtime) VALUES('%s','%s','%s')" % (username,password,int(time.time()*1000))
    effect_row = cursor.execute(sql)
    db.commit()
    cursor.close()
    db.close()
    if(effect_row):
        return json.dumps({'errcode':0,'msg':'注册成功！'})
    else:
        return json.dumps({'errcode':1,'msg':'注册失败！'})




@app.route('/searchfriend',methods=['GET'])
def find_friend_by_username():
    username = request.args.get('username') or ''
    if(username == ''):
        return json.dumps({'errcode':1,'msg':'请输入要查找的账号'})

    db = pymysql.connect("localhost","root","123456","chat" )
    cursor = db.cursor()
    cursor.execute("SELECT nick,username FROM user WHERE username='%s' LIMIT 1" % (username))
    result = cursor.fetchone()
    if(result):
        return json.dumps({'errcode':0,'data':[{'nick':result[0],'username':result[1]}],'msg':'已查到该用户'})
    else:
        return json.dumps({'errcode':0,'data':[],'msg':'未查到该用户'})



@app.route("/addfriend",methods=['POST'])
def add_friend():
    username = request.form.get('username') or ''
    token = request.form.get('token') or ''
    is_login = check_token(username,token)


@app.route("/upphoto",methods=['POST'])
def upphoto():
    pass



if __name__ == '__main__':
    # t = threading.Thread(target=app.run)
    # t.start()
    app.run(debug = True)