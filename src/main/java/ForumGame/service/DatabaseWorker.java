package ForumGame.service;

public interface DatabaseWorker<T,K>{
    void add(T entity);
    void update(K dto, int id);
    void delete(int id);
    
}
