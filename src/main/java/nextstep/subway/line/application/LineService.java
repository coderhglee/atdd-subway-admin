package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = request.toLine();
        line.addStation(stationRepository.findById(request.getUpStationId()).get());
        line.addStation(stationRepository.findById(request.getDownStationId()).get());

        Line persistLine = lineRepository.save(line);
        return LineResponse.of(persistLine);
    }

    public void editLine(Long id, LineRequest request) {
        Line lineById = this.findById(id);
        lineById.update(request.toLine());
        lineRepository.save(lineById);
    }

    public LineResponse findLineById(Long id) {
        return LineResponse.of(this.findById(id));
    }

    @Transactional(readOnly = true)
    public Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
