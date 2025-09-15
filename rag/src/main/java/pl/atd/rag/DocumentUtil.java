package pl.atd.rag;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class DocumentUtil {

  static List<Resource> getDirectoryResources(String directory) throws IOException {
    return Arrays.asList(new PathMatchingResourcePatternResolver().getResources(directory));
  }
}
