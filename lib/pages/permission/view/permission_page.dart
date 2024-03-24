import 'package:flutter/material.dart';

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
              "assets/images/splash.png",
              scale: 3,
            ),
            const SizedBox(
              height: 32.0,
            ),
            const Text('Require Permission'),
            const SizedBox(
              height: 16.0,
            ),
            const Text('''To show your black and white photos
              we just need your folder permission.
              We promise, we don’t take your photos.'''),
            const SizedBox(
              height: 24.0,
            ),
            ElevatedButton(
              onPressed: () {},
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
