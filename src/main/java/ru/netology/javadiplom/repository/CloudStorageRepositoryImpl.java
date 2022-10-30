package ru.netology.javadiplom.repository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.javadiplom.exception.InputException;
import ru.netology.javadiplom.exception.ServerException;
import ru.netology.javadiplom.model.FileData;
import ru.netology.javadiplom.model.User;

import javax.sql.DataSource;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class CloudStorageRepositoryImpl implements CloudStorageRepository {

    private final DataSource source;

    public CloudStorageRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public User login(String login) {
        User auth = null;
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sqlStatement = String.format("SELECT * FROM user WHERE login = '%s'", login);
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new InputException("Error data");
            }
            auth = new User(resultSet.getString("login"), resultSet.getString("password"),
                    resultSet.getString("name"), resultSet.getString("data_base_name"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return auth;
    }

    @Override
    public void loadFile(MultipartFile file, String data_base_name) {
        try (Connection connection = source.getConnection()) {
            String upload_date = new SimpleDateFormat("dd.MM.y k-mm").format(new GregorianCalendar().getTime());

            File tmpFile = File.createTempFile(upload_date, file.getOriginalFilename());
            file.transferTo(tmpFile);
            FileInputStream in = new FileInputStream(tmpFile);
            String sql = String.format("INSERT INTO %s (name, size, upload_date, content) VALUES (?, ?, ?, ?)", data_base_name);
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, file.getOriginalFilename());
            statement.setLong(2, file.getSize());
            statement.setString(3, upload_date);
            statement.setBinaryStream(4, in);
            statement.execute();
            in.close();
            tmpFile.delete();

        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteFile(String fileName, String dataBaseName) {
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sqlStatement = String.format("DELETE FROM %s WHERE name = '%s'", dataBaseName, fileName);
            if (statement.execute(sqlStatement)) {
                throw new ServerException("Error delete file");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Resource getFile(String fileName, String dataBaseName) {
        String pathDir = "C:\\Users\\yurak\\diplomFiles\\";
        Arrays.stream(Objects.requireNonNull(new File(pathDir).listFiles())).filter(File::isFile).forEach(File::delete);

        Resource resource = null;
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM %s WHERE name = '%s'", dataBaseName, fileName);
            ResultSet resultSet = statement.executeQuery(sql);
            if (!resultSet.next()) {
                throw new InputException("Error input data2");
            }
            InputStream inputStream = resultSet.getBinaryStream("content");
            File tmpFile = new File(pathDir + "target_" + fileName);
            FileOutputStream out = new FileOutputStream(tmpFile);
            byte[] buffer = new byte[8 * 1024];
            while (true) {
                int count = inputStream.read(buffer);
                if (count < 0) {
                    break;
                }
                out.write(buffer, 0, count);
            }
            out.close();
            inputStream.close();
            Path path = Paths.get(tmpFile.getAbsolutePath());
            resource = new UrlResource(path.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ServerException("Error download file " + fileName);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return resource;
    }

    @Override
    public void editFile(String oldFileName, String newFileName, String dataBaseName) {
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sqlStatement = String.format("UPDATE %s SET name = '%s' WHERE name = '%s'", dataBaseName, newFileName, oldFileName);
            statement.execute(sqlStatement);
        } catch (SQLException e) {
            throw new ServerException("Error edit file");
        }
    }

    @Override
    public List<FileData> getListOfFile(Integer limit, String dataBaseName) {
        List<FileData> resultList = new ArrayList<>();
        int countLimit = 0;
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            //String sql = String.format("SELECT * FROM data_base_name = '%s'", dataBaseName);
            String sql = "SELECT * FROM " + dataBaseName;
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                if (countLimit < limit) {
                    resultList.add(new FileData(resultSet.getString("name"), resultSet.getLong("size")));
                    countLimit++;
                } else break;
            }
        } catch (SQLException e) {
            throw new ServerException("Error getting file to list");
        }
        return resultList;
    }
}
