import React from "react";

const FilterBox = ({ target, preViewList, handleChange }) => {

  return (
    <div>
      <select name="sortValue" value={target.sortValue} onChange={handleChange}>
        {preViewList.sortValues.map((option) => (
          <option key={option} value={option}>
            {option}
          </option>
        ))}
      </select>

      <select name="sortDirection" value={target.sortDirection} onChange={handleChange}>
        <option value="DESC">DESC</option>
        <option value="ASC">ASC</option>
      </select>
      <br />

      {/* Date Option */}
      <label htmlFor="dateOption">Date Option:</label>
      <select name="dateOption" value={target.dateOption} onChange={handleChange}>
        <option value="">None</option>
        {preViewList.dateOptions.map((option) => (
          <option key={option} value={option}>
            {option}
          </option>
        ))}
      </select>

      {/* Date Start */}
      <label htmlFor="dateStart">Date Start:</label>
      <input
        type="datetime-local"
        name="dateStart"
        value={target.dateStart}
        onChange={handleChange}
      />

      {/* Date End */}
      <label htmlFor="dateEnd">Date End:</label>
      <input
        type="datetime-local"
        name="dateEnd"
        value={target.dateEnd}
        onChange={handleChange}
      />
      <br />

      {/* Size */}
      <label htmlFor="size">Size:</label>
      <input type="number" name="size" value={target.size} onChange={handleChange} />
    </div>
  );
};

export default FilterBox;
