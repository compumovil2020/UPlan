import 'package:flutter/material.dart';
import 'logIn.dart';

void main() => runApp(MaterialApp(
  home: Presentacion(),
  title: "Bienvenido a UPlan - admin",
));

class Presentacion extends StatelessWidget{

  void Siguiente(BuildContext context){
    Navigator.push(
        context, MaterialPageRoute(builder: (context) => logIn())
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Uplan - Administrador"),
        backgroundColor: Colors.purple
      ),
      body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Container(
              width: double.infinity,
              child: Image.asset("assets/images/logo2.png"),
            ),
            Text("¡Bienvenido Admin!", style: TextStyle(fontSize: 30, color:Colors.purple),),
          ]
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.arrow_forward),
        onPressed: () {
          Siguiente(context);
        },
        backgroundColor: Colors.purple,
      ),
    );
  }

}