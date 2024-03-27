import 'package:flutter_test/flutter_test.dart';
import 'package:trp_one/gallery/models/medium.dart';

main() {
  group('Medium model test', () {

    final mediumJson1 = {
      'id': '1',
      'filename': 'selfie',
      'title': 'image with friend',
      'mediumType': 'image',
      'width': 200,
      'height': 200,
      'orientation': 1,
      'mimeType': 'image',
      'creationDate': DateTime.now().millisecondsSinceEpoch,
      'modifiedDate': DateTime.now().millisecondsSinceEpoch
    };
    final mediumJson2 = {
      'id': '2',
      'filename': 'group selfie',
      'title': 'group with friend',
      'mediumType': 'image',
      'width': 200,
      'height': 200,
      'orientation': 1,
      'mimeType': 'image',
      'creationDate': DateTime.now().millisecondsSinceEpoch,
      'modifiedDate': DateTime.now().millisecondsSinceEpoch
    };

    Medium m1 = Medium.fromJson(mediumJson1);

    Medium m2 = Medium.fromJson(mediumJson2);

    Medium m3 = Medium.fromJson(mediumJson1);

    test('medium1 and medium2 data class is not equal',
        () => expect(m1 == m2, isFalse));

    test('medium1 and medium3 data class is not equal',
        () => expect(m1 == m3, isTrue));

    test('medium id is type String', () => expect(m1.id.runtimeType, String));

    test('creationDate should be current day', () => expect(m1.creationDate?.day, DateTime.now().day));
  });
}
