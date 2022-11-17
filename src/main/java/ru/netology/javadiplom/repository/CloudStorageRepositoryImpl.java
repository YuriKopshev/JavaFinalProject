package ru.netology.javadiplom.repository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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


    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final DataSource source;

    public CloudStorageRepositoryImpl(DataSource source) {
        this.source = source;
    }

    @Override
    public User login(String login, String password) {
        User auth = null;
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sqlStatement = String.format("SELECT * FROM user WHERE login = '%s'", login);
            ResultSet resultSet = statement.executeQuery(sqlStatement);
            if (!resultSet.next()) {
                throw new InputException("Error user data");
            }
            if (!passwordEncoder.matches(password, resultSet.getString("password"))) {
                throw new InputException("Wrong password!");
            }

            auth = new User(resultSet.getString("login"), resultSet.getString("password"),
                    resultSet.getString("name"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return auth;
    }

    @Override
    public void loadFile(MultipartFile file, User user) {
        try (Connection connection = source.getConnection()) {
            String upload_date = new SimpleDateFormat("dd.MM.y k-mm").format(new GregorianCalendar().getTime());

            File tmpFile = File.createTempFile(upload_date, file.getOriginalFilename());
            file.transferTo(tmpFile);
            FileInputStream in = new FileInputStream(tmpFile);
            String sql = String.format("INSERT INTO files (name, size, upload_date, content, user_id) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, file.getOriginalFilename());
            statement.setLong(2, file.getSize());
            statement.setString(3, upload_date);
            statement.setBinaryStream(4, in);
            statement.setLong(5, user.getId());
            statement.execute();
            in.close();
            tmpFile.delete();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void deleteFile(String fileName, User user) {
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sqlStatement = String.format("DELETE FROM files WHERE name = '%s' AND user_id = '%s'", fileName, user.getId());
            if (statement.execute(sqlStatement)) {
                throw new ServerException("Error delete file");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Resource getFile(String fileName, User user) {
        String pathDir = "C:\\Users\\yurak\\diplomFiles\\";
        Arrays.stream(Objects.requireNonNull(new File(pathDir).listFiles())).filter(File::isFile).forEach(File::delete);

        Resource resource = null;
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM files WHERE name = '%s' AND user_id = '%s'", fileName, user.getId());
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
    public void editFile(String oldFileName, String newFileName, User user) {
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sqlStatement = String.format("UPDATE files SET name = '%s' WHERE name = '%s' AND user_id = '%s'", newFileName, oldFileName, user.getId());
            statement.execute(sqlStatement);
        } catch (SQLException e) {
            throw new ServerException("Error edit file");
        }
    }

    @Override
    public List<FileData> getListOfFile(Integer limit, User user) {
        List<FileData> resultList = new ArrayList<>();
        int countLimit = 0;
        try (Connection connection = source.getConnection()) {
            Statement statement = connection.createStatement();
            String sql = String.format("SELECT * FROM files WHERE user_id = '%s'", user.getId());
            //String sql = "SELECT * FROM " + "files";
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
