import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_database/firebase_database.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:firebase_auth/firebase_auth.dart';

import 'menu.dart';

class logIn extends StatefulWidget {
  @override
  _LogInState createState() => _LogInState();
}

class _LogInState extends State<logIn>{
  TextEditingController email, password;
  FirebaseAuth auth;
  bool _initialized = false;
  bool _error = false;
  bool _success;
  String _userEmail;
  String message;
  final databaseReference = FirebaseDatabase.instance.reference();

  // Define an async function to initialize FlutterFire
  void initializeFlutterFire() async {
    try {
      // Wait for Firebase to initialize and set `_initialized` state to true
      await Firebase.initializeApp();
      setState(() {
        _initialized = true;
        auth = FirebaseAuth.instance;
      });
    } catch(e) {
      // Set `_error` state to true if Firebase initialization fails
      setState(() {
        _error = true;
      });
    }
  }

  @override
  void initState() {
      initializeFlutterFire();
      email = TextEditingController();
      password = TextEditingController();
      super.initState();
  }

  @override
  Widget build(BuildContext context) {
    // Show error message if initialization failed
    if(_error) {
      return Scaffold(
        body: Column(
            children: [Text("Hubo un grave error"),]
        ),
      );
    }

    // Show a loader until FlutterFire is initialized
    if (!_initialized) {
      return Scaffold(
        body: Column(
            children: [Text("Cargando..."),]
        ),
      );
    }

    return Scaffold(
      appBar: AppBar(
          title: Text("Uplan - Administrador"),
          backgroundColor: Colors.purple
      ),
      body:
          Padding(
            padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                Text(
                  "Inicie Sesión",
                  style: TextStyle(
                    color: Colors.purple,
                    fontSize: 50,
                    ),
                ),
                SizedBox(height: 28,),
                TextField(
                  controller: email,
                  autocorrect: false,
                  decoration: const InputDecoration(
                    labelText: 'Correo Electronico',
                    fillColor: Colors.purple
                  ),
                ),
                SizedBox(height: 5,),
                TextField(
                  controller: password,
                  obscureText: true,
                  enableSuggestions: false,
                  autocorrect: false,
                  decoration: const InputDecoration(
                      labelText: 'Contraseña',
                      fillColor: Colors.purple
                  ),
                ),
                SizedBox(height: 5,),
                Container(
                  width: double.infinity,
                  child:RaisedButton(
                    onPressed: trySignIn,
                    elevation: 20,
                    child: Text("Iniciar Sesión"),
                    color: Colors.purple,
                    textColor: Colors.white,
                  )
                ),
              Container(
                  alignment: Alignment.center,
                  padding: const EdgeInsets.symmetric(horizontal: 4),
                  child: Text(
                    _success == null ? ''
                        : (_success
                        ? 'Se ingreso a ' + _userEmail + ' exitosamente'
                        : message),
                    style: TextStyle(color: Colors.red),
                  ),
              ),
              ]
            )
    )
    );
  }

  Future<void> trySignIn() async {
    if(email.text.isEmpty || password.text.isEmpty)
    {
      setState(() {
        _success = false;
        message = "Estan vacíos los campos";
      });
    }
    else{
      try {
        final User user = (await auth.signInWithEmailAndPassword(email: email.text, password: password.text,)).user;
        if (user != null) {
          final bool isAdm = await isAdmin(user.uid);
            if(isAdm == true) {
              setState(() {
                _userEmail = user.email;
                _success = true;
                irAlMenu(context);
              });
            }
            else {
              setState(() {
                _success = false;
                message = "Se ingreso a " + user.email +
                    " exitosamente,\n pero no tiene acceso a funciones de admin";
              });
            }
        }
      }on FirebaseAuthException catch (e) {
        if (e.code == 'user-not-found') {
          setState(() {
            _success = false;
            message = 'No existe usuario con ese mail.';
          });
        } else if (e.code == 'wrong-password') {
          setState(() {
            _success = false;
            message = 'Contraseña equivocada para ese usuario.';
          });
        } else if (e.code == 'invalid-email') {
          setState(() {
            _success = false;
            message = 'El formato del mail no es valido.';
          });
        }
      }
    }
  }

  Future<bool> isAdmin(String uid) async {
    return await databaseReference.child("admins").once().then((DataSnapshot snapshot) {
      try {
        print(snapshot.value[uid]);
        if (snapshot.value[uid] != null) {
          print(true);
          return true;
        }
        else {
          print (false);
          return false;
        }
      }catch(error)
      {
        print (false);
        return false;
      }
    });
  }

  void irAlMenu(BuildContext context){
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => Menu())
    );
  }
}