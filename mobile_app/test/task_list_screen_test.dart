import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:mocktail/mocktail.dart';
import 'package:mobile_app/task.dart';
import 'package:mobile_app/task_api_service.dart';
import 'package:mobile_app/main.dart';

class MockTaskApiService extends Mock implements TaskApiService {}

void main() {
  late MockTaskApiService mockApiService;

  setUp(() {
    mockApiService = MockTaskApiService();
  });

  testWidgets('shows a loading indicator then the fetched tasks', (WidgetTester tester) async {
    when(() => mockApiService.getTasks()).thenAnswer(
      (_) async => [
        Task(id: 1, title: 'Buy milk', description: '2% please', completed: false),
        Task(id: 2, title: 'Walk the dog', description: '', completed: true),
      ],
    );

    await tester.pumpWidget(MaterialApp(home: TaskListScreen(apiService: mockApiService)));

    // Immediately after pumping, the loading spinner should show.
    expect(find.byType(CircularProgressIndicator), findsOneWidget);

    // Let the fake async getTasks() call resolve.
    await tester.pumpAndSettle();

    expect(find.text('Buy milk'), findsOneWidget);
    expect(find.text('Walk the dog'), findsOneWidget);
  });

  testWidgets('shows an error message when the API call fails', (WidgetTester tester) async {
    when(() => mockApiService.getTasks()).thenThrow(Exception('network error'));

    await tester.pumpWidget(MaterialApp(home: TaskListScreen(apiService: mockApiService)));
    await tester.pumpAndSettle();

    expect(find.textContaining('Could not load tasks'), findsOneWidget);
    expect(find.text('Retry'), findsOneWidget);
  });

  testWidgets('shows empty state when there are no tasks', (WidgetTester tester) async {
    when(() => mockApiService.getTasks()).thenAnswer((_) async => []);

    await tester.pumpWidget(MaterialApp(home: TaskListScreen(apiService: mockApiService)));
    await tester.pumpAndSettle();

    expect(find.text('No tasks yet — tap + to add one'), findsOneWidget);
  });
}
