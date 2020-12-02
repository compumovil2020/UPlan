import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_database/ui/firebase_animated_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class newAdmins extends StatefulWidget
{
  @override
  _newAdminsState createState() => _newAdminsState();
}

class _newAdminsState extends State<newAdmins> {
  Query _ref;
  final _scaffoldKey = GlobalKey<ScaffoldState>();

  void AddAdmin(String uid, String username)
  {
      FirebaseDatabase.instance.reference().child("admins").child(uid).set(username);
      _scaffoldKey.currentState.showSnackBar(
        SnackBar(
          content: const Text('Se ha agregado un nuevo admin.'),
          action: SnackBarAction(
              label: 'X', onPressed: _scaffoldKey.currentState.hideCurrentSnackBar),
        ),
      );
  }

  Widget _buildUserItem({Map user})
  {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 10),
      padding: EdgeInsets.all(10),
      height: 160,
      color: Colors.white,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(children: [
            Icon(Icons.person, color: Colors.purple, size: 20,),
            SizedBox(width: 6,),
            Text(user['username']+" - "+ user['nombre'] + " "+user['apellido'], style: TextStyle(fontSize: 15, color: Colors.purple)),
            SizedBox(width: 6,),
          ]),
          SizedBox(height: 10,),
          Row(children: [
            Icon(Icons.phone_iphone, color: Colors.deepOrange, size: 20,),
            SizedBox(width: 6,),
            Text(user['email'], style: TextStyle(fontSize: 15, color: Colors.deepOrange)),
            ]
          ),
          SizedBox(height: 10,),
          Row(children: [
            Icon(Icons.group_work, color: Colors.lightGreen, size: 20,),
            Text(user['latitud'].toString() + ":" +user['longitud'].toString(), style: TextStyle(fontSize: 15, color: Colors.lightGreen)),
          ]),
          SizedBox(height: 10,),
          Row(
            children: [
              Expanded(
                flex: 10,
                child: Container(color: Colors.red),
              ),
              RaisedButton(
                onPressed: () {
                  setState(() {
                    AddAdmin(user['id'], user['username']);
                  });
                },
                elevation: 20,
                child: Icon(Icons.add),
                color: Colors.purple,
                textColor: Colors.white,
              )
          ]),
        ],
      ),
    );
  }

  @override
  void initState() {
    _ref = FirebaseDatabase.instance.reference().child("usuarios").orderByChild("email");
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      key: _scaffoldKey,
      appBar: AppBar(
        title: Text("Uplan - Administrador"),
        backgroundColor: Colors.purple,
      ),
      body: Container(
        height: double.infinity,
        child: FirebaseAnimatedList(query: _ref, itemBuilder: (BuildContext context, DataSnapshot snapshot, Animation<double> animation, int index){
          Map user = snapshot.value;
          return _buildUserItem(user: user);
        },),
      ),
    );
  }

}