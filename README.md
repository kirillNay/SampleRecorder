# SampleRecorder

## Приложение полуфинала для Yandex Cup 2023

При всей минималистичности интерфейса приложение даёт широкие возможности: можно выбирать инструменты и управлять их звучанием, сводить вместе несколько аудиодорожек, добавлять звук с микрофона и проигрывать готовую композицию.

##### Основные механики

UI приложения представлен единственным экраном, на котором находятся следующие элементы:
###### Выбор инструмента
С помощью этих контролов можно переключать вид инструмента, а также по лонгтапу выбирать один из нескольких вариантов. По зажатию проигрывается и выбирается семпл.
###### Рабочая область для настройки звучания
Двигая пальцем по экрану, мы можем настроить громкость и скорость повторения семплов для выбранного инструмента. На макете вы видите способ управления с помощью ползунков, но можно выбрать и другой.
###### Область визуализации
Здесь вы видите изображение звукового ряда, которое меняется, когда музыка играет. Например, оно может показывать изменения громкости.
###### Нижняя панель инструментов
Слева находится контрол для переключения между слоями (звуковыми рядами). Справа — кнопка для записи звука с микрофона, кнопки для записи и воспроизведения трека.

Чтобы создавать музыку, нужно настроить звучание нескольких слоёв звукового ряда, который состоит из семплов определённого инструмента. Можно накладывать отдельно звук с микрофона. Например, мы можем выбрать ударный инструмент, настроить его темп и громкость. Затем добавить новый слой с гитарной партией. Затем записать поверх этого вокальную партию или даже мяуканье кота. И, когда всё готово, записываем итоговый трек, управляя через слои моментом вступления того или иного инструмента.
