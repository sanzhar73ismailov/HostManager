v. 2016.06.05-1 
+  в проект lab добавил новый веб-сервисный метод getResultsByInstrumentAndDate для получения результатов по прибору
~ в проект CommPort - в классе instrument.DriverAdvia2120 в методе getResultsFromMessage добавил строки по нарщиванию версии результата
                int lastVersion = model.getVersionOfLastResultByInstrumentAndSid(instrument.getName(), message.getSid(), testCode);
                resEntity.setVersion(lastVersion + 1);
v. 2016.06.27-1 
~ DoReport поменял, чтобы в отчет попадали результаты прибора не только последние, но и по версиям