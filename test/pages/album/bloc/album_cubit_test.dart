import 'package:flutter_test/flutter_test.dart';
import 'package:trp_one/pages/album/bloc/album_bloc.dart';
import 'package:trp_one/pages/album/bloc/album_state.dart';

main(){

  group('AlbumCubit', () {
    AlbumBloc albumBloc() => AlbumBloc();

    test('Constructor works properly', () =>{
      expect(albumBloc, returnsNormally)
    });

    test('Value of loading is true', (){
      expect(albumBloc().state.loading, isTrue);
    });

    test('Constructor has correct initial state', () =>{
      expect(albumBloc().state, equals(const AlbumState(loading: true)))
    });
  });

}