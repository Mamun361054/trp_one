import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import '../bloc/splash_bloc.dart';

class SplashScreen extends StatefulWidget {
  const SplashScreen({super.key});

  @override
  State<SplashScreen> createState() => _SplashScreenState();
}

class _SplashScreenState extends State<SplashScreen> {
  @override
  Widget build(BuildContext context) {

    return BlocProvider(
      create: (context) => SplashBloc(context: context),
      child: Scaffold(
        body: Center(
          child: Image.asset("assets/images/splash.png",scale: 3,),
        ),
      ),
    );
  }
}
