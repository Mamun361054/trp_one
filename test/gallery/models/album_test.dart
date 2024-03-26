import 'package:flutter_test/flutter_test.dart';
import 'package:trp_one/gallery/common/medium_type.dart';
import 'package:trp_one/gallery/models/album.dart';

main() {
  group('Album model test', () {
    final albumJson1 = {
      'id': '1',
      'name': 'Recent',
      'count': 23,
    };

    final albumJson2 = {
      'id': '2',
      'name': 'Downloads',
      'count': 25,
    };

    Album a1 = Album.fromJson(albumJson1, MediumType.image, true);

    Album a2 = Album.fromJson(albumJson2, MediumType.image, true);

    Album a3 = Album.fromJson(albumJson1, MediumType.image, true);

    test('album1 and album2 data class is not equal', () => expect(a1 == a2, isFalse));

    test('album1 and album3 data class is not equal', () => expect(a1 == a3, isTrue));

    test('album id is type String', () => expect(a1.id.runtimeType, String));
  });
}
