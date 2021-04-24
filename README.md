#ИС Host Manager

##Сервис по работе с лабораторными анализаторами

##OS Linux, Windows

##IDE Netbeans 8.01
##AS GlassFish 4.1

##Версия ведется в lab\src\conf\MANIFEST.MF
Формат: `yy.mm.dd.idx`
`yy`  - год, до 2х знаков
`mm`  - месяц, до 2х знаков
`dd`  - день, до 2х знаков
`idx` - порядковый номер ревизии в текущем дне, начиная с 1

##Содержимое
   netbeans/lab/ - Главный проект - веб приложение для администрирования
   netbeans/CommPort/ - модуль для обмена данными с приборами
   DB - скрипты DDL,DML
   DOC - документация

##История изменений (расти вверх)
v. 21.04.24.1
   + в проекте lab - добавлен драйвер sysmexXS500i 
   + в проект CommPort - добавлен драйвер sysmexXS500i 
v. 21.04.10.1
   ~ в проекте lab - рефакторинг
   ~ в проект CommPort - рефакторинг
v. 18.05.14.1
   ~ DriverDimensionXpand.getResultsFromMessage
     теперь в результаты в базу попадают все результаты, не только с простыми ошибками, но и с SuppressResult ошибками.
v. 16.06.27.1 
   ~ DoReport поменял, чтобы в отчет попадали результаты прибора не только последние, но и по версиям
v. 16.06.05.1 
   + в проект lab добавил новый веб-сервисный метод getResultsByInstrumentAndDate для получения результатов по прибору
   ~ в проект CommPort - в классе instrument.DriverAdvia2120 в методе getResultsFromMessage добавил строки по нарщиванию версии результата
                int lastVersion = model.getVersionOfLastResultByInstrumentAndSid(instrument.getName(), message.getSid(), testCode);
                resEntity.setVersion(lastVersion + 1);





