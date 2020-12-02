import 'package:firebase_database/firebase_database.dart';
import 'package:firebase_database/ui/firebase_animated_list.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class Reports extends StatefulWidget
{
  @override
  _ReportsState createState() => _ReportsState();
}

class _ReportsState extends State<Reports> {
  Query _ref;
  final _scaffoldKey = GlobalKey<ScaffoldState>();

  Widget _buildUserItem({Map event, String key})
  {
    return Container(
      margin: EdgeInsets.symmetric(vertical: 10),
      padding: EdgeInsets.all(10),
      height: 190,
      color: Colors.white,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(children: [
            Icon(Icons.person, color: Colors.purple, size: 20,),
            SizedBox(width: 6,),
            Text(event['nombreEv'] +" de "+event['nombrePerf'], style: TextStyle(fontSize: 15, color: Colors.purple)),
            SizedBox(width: 6,),
          ]),
          SizedBox(height: 10,),
          Row(children: [
            Icon(Icons.preview_sharp, color: Colors.deepOrange, size: 20,),
            SizedBox(width: 6,),
            Text(event['tipo'], style: TextStyle(fontSize: 15, color: Colors.deepOrange)),
          ]
          ),
          SizedBox(height: 10,),
          Row(children: [
            Icon(Icons.event_note_sharp, color: Colors.blue, size: 20,),
            Text(event['descripcion'], style: TextStyle(fontSize: 15, color: Colors.blue)),
            ]),
          SizedBox(height: 10,),
          Row(children: [
            Icon(Icons.location_city, color: Colors.lightGreen, size: 20,),
            Text(event['venue'], style: TextStyle(fontSize: 15, color: Colors.lightGreen)),
            Text("("+event['latitud'].toString() + ":" +event['longitud'].toString()+")", style: TextStyle(fontSize: 10, color: Colors.lightGreen)),
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
                      discardReport(key);
                    });
                  },
                  elevation: 20,
                  child: Icon(Icons.check_sharp),
                  color: Colors.green,
                  textColor: Colors.white,
                ),
                RaisedButton(
                  onPressed: () {
                    setState(() {
                      deleteEvent(key);
                    });
                  },
                  elevation: 20,
                  child: Icon(Icons.clear_sharp),
                  color: Colors.red,
                  textColor: Colors.white,
                )
              ]),
        ],
      ),
    );
  }

  void deleteEvent(String eventid){
    FirebaseDatabase.instance.reference().child("eventos").child(eventid).remove();
    _scaffoldKey.currentState.showSnackBar(
      SnackBar(
        content: const Text('Se ha borrado el evento.'),
        action: SnackBarAction(
            label: 'X', onPressed: _scaffoldKey.currentState.hideCurrentSnackBar),
      ),
    );
  }

  void discardReport(String eventid){
    FirebaseDatabase.instance.reference().child("eventos").child(eventid).child('reportado').set(false);
    _scaffoldKey.currentState.showSnackBar(
      SnackBar(
        content: const Text('Se ha descartado el reporte.'),
        action: SnackBarAction(
            label: 'X', onPressed: _scaffoldKey.currentState.hideCurrentSnackBar),
      ),
    );
  }

  @override
  void initState() {
    _ref = FirebaseDatabase.instance.reference().child("eventos").orderByChild("reportado");
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
          Map event = snapshot.value;
          print(event['reportado']);
          if(event['reportado'] == true) {
            return _buildUserItem(event: event, key: snapshot.key);
          }else{
            return Container();
          }
        },),
      ),
    );
  }

}