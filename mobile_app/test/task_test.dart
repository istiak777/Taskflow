import 'package:flutter_test/flutter_test.dart';
import 'package:mobile_app/task.dart';

void main() {
  group('Task', () {
    test('fromJson parses a full JSON object correctly', () {
      final json = {
        'id': 1,
        'title': 'Buy milk',
        'description': '2% please',
        'completed': false,
      };

      final task = Task.fromJson(json);

      expect(task.id, 1);
      expect(task.title, 'Buy milk');
      expect(task.description, '2% please');
      expect(task.completed, false);
    });

    test('fromJson handles missing description and completed gracefully', () {
      final json = {'id': 2, 'title': 'Just a title'};

      final task = Task.fromJson(json);

      expect(task.description, '');
      expect(task.completed, false);
    });

    test('toJson produces the expected map, excluding id', () {
      final task = Task(title: 'Test task', description: 'desc', completed: true);

      final json = task.toJson();

      expect(json['title'], 'Test task');
      expect(json['description'], 'desc');
      expect(json['completed'], true);
      expect(json.containsKey('id'), false);
    });

    test('copyWith overrides only specified fields', () {
      final original = Task(id: 5, title: 'Original', description: 'desc', completed: false);

      final updated = original.copyWith(completed: true);

      expect(updated.id, 5);
      expect(updated.title, 'Original');
      expect(updated.completed, true);
    });
  });
}
