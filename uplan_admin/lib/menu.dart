import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/material.dart';
import 'package:uplan_admin/logIn.dart';
import 'package:uplan_admin/notAvailable.dart';
import 'package:uplan_admin/reports.dart';

import 'createAdmins.dart';


class Menu extends StatelessWidget{

  final FirebaseAuth _firebaseAuth = FirebaseAuth.instance;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          automaticallyImplyLeading: false,
          title: Text("Menú Administrador"),
          backgroundColor: Colors.purple,
          actions: <Widget>[
              Padding(
              padding: EdgeInsets.only(right: 20.0),
                child: GestureDetector(
                  onTap: () => logOut(context),
                  child: Icon(
                    Icons.logout,
                    size: 26.0,
                  ),
                )
              ),
          ],
      ),
      body:
      Padding(
          padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Container(
                width: 200,
                height: 200,
                child: Image.network("https://image.flaticon.com/icons/png/512/1476/premium/1476224.png", fit: BoxFit.fill,
                  loadingBuilder: (context,child,progress){
                  if (progress == null) return child;
                  return Center(
                    child: CircularProgressIndicator(
                      value: progress.expectedTotalBytes != null ?
                      progress.cumulativeBytesLoaded / progress.expectedTotalBytes
                          : null,
                    ),
                  );
                },
                ),
              ),
              Container(
                  width: double.infinity,
                  child:RaisedButton(
                    onPressed: () => Reportes(context),
                    elevation: 20,
                    child: Text("Manejar Reportes"),
                    color: Colors.purple,
                    textColor: Colors.white,
                  )
              ),
              SizedBox(height: 10,),
              Container(
                  width: double.infinity,
                  child:RaisedButton(
                    onPressed: () => NewAdmins(context),
                    elevation: 20,
                    child: Text("Asignar Nuevos Admins"),
                    color: Colors.purple,
                    textColor: Colors.white,
                  )
              ),
              SizedBox(height: 10,),
              Container(
                  width: double.infinity,
                  child:RaisedButton(
                    onPressed: () => MergeEvents(context),
                    elevation: 20,
                    child: Text("Mezclar Eventos"),
                    color: Colors.purple,
                    textColor: Colors.white,
                  )
              ),
            ]
        ),
      ),
    );
  }

  void Reportes(BuildContext context){
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => Reports())
    );
  }

  void NewAdmins(BuildContext context){
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => newAdmins())
    );
  }

  void MergeEvents(BuildContext context){
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => notAvailable())
    );
  }

  Future<void> logOut(BuildContext context) async {
    await _firebaseAuth.signOut();
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => logIn())
    );
  }

}