import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bloc/flutter_bloc.dart';
import 'package:trp_one/pages/permission/view/permission_page.dart';
import 'package:trp_one/pages/splash/bloc/splash_state.dart';

class SplashBloc extends Cubit<SplashState> {
  SplashBloc({required BuildContext context}) : super(const SplashState()) {}
}
