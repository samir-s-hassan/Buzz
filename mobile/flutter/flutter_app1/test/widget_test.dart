import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:flutter_app1/main.dart';

void main() {
  testWidgets('Testing Widgets', (WidgetTester tester) async {
    //Doesnt work cause HTTP request
    await tester.pumpWidget(const MyApp());
    expect(find.text('The Buzz'), findsOneWidget);
    expect(find.byIcon(Icons.add), findsOneWidget);
  });
}
