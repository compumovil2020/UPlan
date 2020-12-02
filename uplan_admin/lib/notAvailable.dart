import 'package:flutter/material.dart';

class notAvailable extends StatelessWidget{

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
          title: Text("Uplan - Administrador"),
          backgroundColor: Colors.purple
      ),
      body: Padding(
          padding: EdgeInsets.fromLTRB(20, 0, 20, 0),
          child:Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: [
              Container(
                width: 200,
                height: 200,
                child: Image.network("https://static.thenounproject.com/png/42732-200.png", fit: BoxFit.fill,
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
              Text("Estamos trabajando en ello", style: TextStyle(fontSize: 20, color:Colors.purple),),
            ]
      ),
      )
    );
  }

}