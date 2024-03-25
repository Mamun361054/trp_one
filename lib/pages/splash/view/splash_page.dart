import 'package:flutter/material.dart';
import 'package:trp_one/pages/permission/view/permission_page.dart';

class SplashScreen extends StatefulWidget {

  static Route route(){
    return MaterialPageRoute(builder: (_) => const SplashScreen());
  }

  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {

  void initSplash() {
    Future.delayed(const Duration(seconds: 2), () {
      Navigator.push(context, MaterialPageRoute(builder: (_)=> const PermissionPage()));
    });
  }

  @override
  void initState() {
    super.initState();
    initSplash();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Image.asset("assets/images/splash.png",scale: 3,),
      ),
    );
  }
}
