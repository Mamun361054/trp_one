import 'package:flutter/material.dart';
import 'package:trp_one/pages/album/view/album_screen.dart';

class PermissionPage extends StatelessWidget {
  const PermissionPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Image.asset(
              "assets/images/permission.png",
              scale: 3,
            ),
            const SizedBox(
              height: 32.0,
            ),
            const Text(
              'Require Permission',
              style: TextStyle(
                  fontSize: 20.0,
                  fontWeight: FontWeight.w400,
                  color: Colors.black),
            ),
            const SizedBox(
              height: 16.0,
            ),
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 32.0),
              child: Text('To show your black and white photos we just need your folder permission.We promise, we don’t take your photos.',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                      fontSize: 14.0,
                      fontWeight: FontWeight.w400,
                      color: Colors.grey)),
            ),
            const SizedBox(
              height: 24.0,
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (_)=> const AlbumScreen()));
              },
              style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xff66FFB6),
                  shape: const StadiumBorder()),
              child: const Text('Grant Access'),
            )
          ],
        ),
      ),
    );
  }
}
